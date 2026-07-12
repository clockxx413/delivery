// ============================================================
// ROUTING
// ============================================================
let currentView = '';

function switchView(view) {
  document.querySelectorAll('.view').forEach(v => v.classList.remove('active'));
  const el = document.getElementById('view-' + view);
  if (el) el.classList.add('active');
  currentView = view;
  renderNavbar();
  renderView(view);
  window.scrollTo(0, 0);
}

function goHome() {
  if (!currentUser) { switchView('login'); return; }
  const map = { user: 'userHome', runner: 'runnerHall', admin: 'adminDashboard' };
  switchView(map[currentUser.role] || 'login');
}

function renderView(view) {
  switch (view) {
    case 'login': renderLogin(); break;
    case 'userHome': renderUserHome(); break;
    case 'userOrders': renderUserOrders(); break;
    case 'runnerHall': renderRunnerHall(); break;
    case 'runnerDeliveries': renderRunnerDeliveries(); break;
    case 'runnerApply': renderRunnerApply(); break;
    case 'runnerEarnings': renderRunnerEarnings(); break;
    case 'runnerWithdraw': renderRunnerWithdraw(); break;
    case 'adminDashboard': renderAdminDashboard(); break;
    case 'adminUsers': renderAdminUsers(); break;
    case 'adminOrders': renderAdminOrders(); break;
    case 'adminRunners': renderAdminRunners(); break;
    case 'adminViolations': renderAdminViolations(); break;
    case 'adminSettings': renderAdminSettings(); break;
    case 'adminStats': renderAdminStats(); break;
  }
}

function renderNavbar() {
  const nav = document.getElementById('navbarNav');
  const userEl = document.getElementById('navbarUser');
  if (!currentUser) {
    nav.innerHTML = '';
    userEl.innerHTML = '<button class="btn btn-primary btn-sm" onclick="switchView(\'login\')">登录</button>';
    return;
  }

  const u = currentUser;
  const roleMap = { user: '用户', runner: '跑腿', admin: '管理' };
  userEl.innerHTML = `
    <div class="avatar">${u.avatar}</div>
    <span class="user-name">${u.name}</span>
    <span class="user-role ${u.role}">${roleMap[u.role]}</span>
    <button class="btn btn-ghost btn-sm" onclick="logout()" style="margin-left:4px;">退出</button>
  `;

  let navHTML = '';
  if (u.role === 'user') {
    navHTML = `
      <button class="${currentView==='userHome'?'active':''}" onclick="switchView('userHome')">🏠 首页</button>
      <button class="${currentView==='userOrders'?'active':''}" onclick="switchView('userOrders')">📋 我的订单</button>
      ${!u.runnerApplication || u.runnerApplication.status === 'rejected'
        ? `<button class="${currentView==='runnerApply'?'active':''}" onclick="switchView('runnerApply')" style="color:var(--warning);">🎯 申请跑腿</button>`
        : u.runnerApplication.status === 'pending'
        ? '<span style="font-size:11px;color:var(--text-muted);padding:8px;">⏳ 跑腿审核中</span>'
        : ''}
    `;
  } else if (u.role === 'runner') {
    const pendingCount = getOrders().filter(o => o.status === 'pending').length;
    const myActive = getOrders().filter(o => o.runnerId === u.id && !['delivered','rated'].includes(o.status)).length;
    navHTML = `
      <button class="${currentView==='runnerHall'?'active':''}" onclick="switchView('runnerHall')">📋 订单大厅 ${pendingCount ? '<b style="color:var(--danger)">'+pendingCount+'</b>' : ''}</button>
      <button class="${currentView==='runnerDeliveries'?'active':''}" onclick="switchView('runnerDeliveries')">🏃 我的配送 ${myActive ? '<b style="color:var(--warning)">'+myActive+'</b>' : ''}</button>
      <button class="${currentView==='runnerEarnings'?'active':''}" onclick="switchView('runnerEarnings')">💰 收入</button>
      <button class="${currentView==='runnerWithdraw'?'active':''}" onclick="switchView('runnerWithdraw')">💳 提现</button>
    `;
  } else if (u.role === 'admin') {
    navHTML = `
      <button class="${currentView==='adminDashboard'?'active':''}" onclick="switchView('adminDashboard')">📊 仪表盘</button>
      <button class="${currentView==='adminUsers'?'active':''}" onclick="switchView('adminUsers')">👥 用户</button>
      <button class="${currentView==='adminRunners'?'active':''}" onclick="switchView('adminRunners')">🏃 跑腿</button>
      <button class="${currentView==='adminOrders'?'active':''}" onclick="switchView('adminOrders')">📦 订单</button>
      <button class="${currentView==='adminViolations'?'active':''}" onclick="switchView('adminViolations')">⚠ 违规</button>
      <button class="${currentView==='adminSettings'?'active':''}" onclick="switchView('adminSettings')">⚙ 设置</button>
      <button class="${currentView==='adminStats'?'active':''}" onclick="switchView('adminStats')">📈 统计</button>
    `;
  }
  nav.innerHTML = navHTML;
}

// ============================================================
// HELPERS
// ============================================================
function formatTime(ts) {
  if (!ts) return '-';
  const d = new Date(ts);
  if (isNaN(d.getTime())) return ts;
  return d.toLocaleString('zh-CN', { month:'numeric', day:'numeric', hour:'2-digit', minute:'2-digit' });
}
function now() { return new Date().toISOString().slice(0, 16).replace('T', ' '); }
function genOrderId() {
  const d = new Date();
  return 'DD' + d.getFullYear() + String(d.getMonth()+1).padStart(2,'0') + String(d.getDate()).padStart(2,'0') + String(d.getTime()%100000).padStart(5,'0');
}
function statusLabel(s) {
  const map = { pending:'待接单', accepted:'已接单', picked_up:'已取件', delivering:'配送中', delivered:'已送达', rated:'已评价' };
  return map[s] || s;
}
const STATUS_ORDER = ['pending','accepted','picked_up','delivering','delivered','rated'];
function statusIdx(s) { return STATUS_ORDER.indexOf(s); }

function showToast(msg) {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.classList.add('show');
  clearTimeout(t._tid);
  t._tid = setTimeout(() => t.classList.remove('show'), 2200);
}

function openModal(title, bodyHTML, actionsHTML) {
  document.getElementById('modalTitle').textContent = title;
  document.getElementById('modalBody').innerHTML = bodyHTML;
  document.getElementById('modalActions').innerHTML = actionsHTML || '';
  document.getElementById('modal').classList.add('show');
}
function closeModal() {
  document.getElementById('modal').classList.remove('show');
}
document.getElementById('modal').addEventListener('click', function(e) {
  if (e.target === this) closeModal();
});
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') closeModal();
});

function renderStars(rating, interactive, callback) {
  let html = '<div class="stars" data-rating="' + rating + '">';
  for (let i = 1; i <= 5; i++) {
    html += '<span class="star' + (i <= rating ? ' active' : '') + '" data-star="' + i + '">★</span>';
  }
  html += '</div>';
  if (interactive) {
    setTimeout(() => {
      const starsEl = document.querySelector('.stars');
      if (!starsEl) return;
      starsEl.querySelectorAll('.star').forEach(s => {
        s.addEventListener('click', function() {
          const v = parseInt(this.dataset.star);
          starsEl.setAttribute('data-rating', v);
          starsEl.querySelectorAll('.star').forEach(ss => ss.classList.toggle('active', parseInt(ss.dataset.star) <= v));
          if (callback) callback(v);
        });
        s.addEventListener('mouseenter', function() {
          const v = parseInt(this.dataset.star);
          starsEl.querySelectorAll('.star').forEach(ss => ss.classList.toggle('active', parseInt(ss.dataset.star) <= v));
        });
      });
      starsEl.addEventListener('mouseleave', function() {
        const v = parseInt(starsEl.getAttribute('data-rating'));
        starsEl.querySelectorAll('.star').forEach(ss => ss.classList.toggle('active', parseInt(ss.dataset.star) <= v));
      });
    }, 50);
  }
  return html;
}

function getRunnerName(runnerId) {
  if (!runnerId) return '-';
  const u = getUsers().find(u => u.id === runnerId);
  return u ? u.name : '未知';
}
function getUserName(userId) {
  if (!userId) return '-';
  const u = getUsers().find(u => u.id === userId);
  return u ? u.name : '未知';
}

// ============================================================
// SEARCH / FILTER
// ============================================================
function filterByKeyword(items, keyword, fields) {
  if (!keyword || !keyword.trim()) return items;
  const kw = keyword.trim().toLowerCase();
  return items.filter(item => {
    return fields.some(f => {
      const val = item[f];
      if (val == null) return false;
      return String(val).toLowerCase().includes(kw);
    });
  });
}

function renderSearchBar(placeholder, inputId, onInputFn) {
  return `
    <div class="search-bar">
      <span class="search-icon">🔍</span>
      <input type="text" class="search-input" id="${inputId}"
             placeholder="${placeholder || '搜索订单号/取件点/送达点...'}"
             oninput="${onInputFn}(this.value)">
      ${onInputFn ? `<button class="search-clear" onclick="document.getElementById('${inputId}').value='';${onInputFn}('');" style="display:none;" id="${inputId}_clear">✕</button>` : ''}
    </div>
  `;
}

