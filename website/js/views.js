// ============================================================
// VIEW: LOGIN / REGISTER
// ============================================================
let authMode = 'login'; // 'login' | 'register'
let registerRole = 'user';

function renderLogin() {
  document.getElementById('view-login').innerHTML = `
    <div class="auth-wrapper">
      <div class="auth-card">
        <h2>${authMode === 'login' ? '欢迎回来 👋' : '创建账号 🎉'}</h2>
        <p class="subtitle">${authMode === 'login' ? '使用学号登录校园快递代取平台' : '加入校园快递代取平台'}</p>
        <div class="auth-tabs">
          <button class="${authMode==='login'?'active':''}" onclick="authMode='login';renderLogin();">登录</button>
          <button class="${authMode==='register'?'active':''}" onclick="authMode='register';renderLogin();">注册</button>
        </div>
        ${authMode === 'register' ? `
        <div class="form-group">
          <label>选择角色</label>
          <div class="role-select" id="roleSelect">
            <div class="role-option ${registerRole==='user'?'selected':''}" data-role="user" onclick="selectRole('user')">
              <span class="role-icon">👤</span>普通用户
            </div>
            <div class="role-option ${registerRole==='runner'?'selected':''}" data-role="runner" onclick="registerRole='runner';renderLogin();">
              <span class="role-icon">🏃</span>跑腿员
            </div>
          </div>
          ${registerRole==='runner' ? '<p class="form-hint">💡 注册即提交跑腿申请，需管理员审核通过后方可接单</p>' : ''}
        </div>
        ` : ''}
        <div class="form-group">
          <label>学号</label>
          <input type="text" id="authStudentId" placeholder="请输入学号">
        </div>
        ${authMode === 'register' ? `
        <div class="form-row">
          <div class="form-group">
            <label>姓名</label>
            <input type="text" id="authName" placeholder="请输入姓名">
          </div>
          <div class="form-group">
            <label>手机号（选填）</label>
            <input type="text" id="authPhone" placeholder="选填" maxlength="11">
          </div>
        </div>
        ` : ''}
        <div class="form-group">
          <label>密码</label>
          <input type="password" id="authPassword" placeholder="请输入密码">
        </div>
        <div class="error-msg" id="authError"></div>
        <button class="btn btn-primary btn-block btn-lg" style="margin-top:8px;" onclick="handleAuth()">
          ${authMode === 'login' ? '登录' : registerRole==='runner' ? '提交申请' : '注册'}
        </button>
        ${authMode === 'login' ? `
        <div style="margin-top:20px;padding:16px;background:#F9FAFB;border-radius:8px;font-size:12px;color:var(--text-secondary);line-height:1.8;">
          <b style="color:var(--text);">🔑 演示账号（用学号登录）：</b><br>
          👤 用户：2024001001 / 123456（张三）<br>
          🏃 跑腿：2023002001 / 123456（李小明）<br>
          🔧 管理：admin001 / admin
        </div>
        ` : ''}
      </div>
    </div>
  `;
}

function selectRole(role) {
  registerRole = role;
  renderLogin();
}

function handleAuth() {
  const studentId = document.getElementById('authStudentId').value.trim();
  const password = document.getElementById('authPassword').value.trim();
  const errEl = document.getElementById('authError');

  if (!studentId) { errEl.style.display = 'block'; errEl.textContent = '请输入学号'; return; }
  if (!password) { errEl.style.display = 'block'; errEl.textContent = '请输入密码'; return; }

  if (authMode === 'login') {
    const result = login(studentId, password);
    if (!result.ok) { errEl.style.display = 'block'; errEl.textContent = result.msg; return; }
    errEl.style.display = 'none';
    showToast('✅ 登录成功！欢迎，' + result.user.name);
    goHome();
  } else {
    const name = document.getElementById('authName').value.trim();
    const phone = document.getElementById('authPhone') ? document.getElementById('authPhone').value.trim() : '';
    if (!name) { errEl.style.display = 'block'; errEl.textContent = '请输入姓名'; return; }
    if (password.length < 6) { errEl.style.display = 'block'; errEl.textContent = '密码至少6位'; return; }
    const result = register(name, studentId, phone, password, registerRole);
    if (!result.ok) { errEl.style.display = 'block'; errEl.textContent = result.msg; return; }
    errEl.style.display = 'none';
    if (registerRole === 'runner') {
      showToast('🎉 跑腿申请已提交，等待管理员审核');
    } else {
      showToast('🎉 注册成功！欢迎加入，' + result.user.name);
    }
    goHome();
  }
}

// Allow Enter to submit
document.addEventListener('keydown', function(e) {
  if (e.key === 'Enter' && currentView === 'login') {
    handleAuth();
  }
});

// ============================================================
// VIEW: USER HOME
// ============================================================
function renderUserHome() {
  const orders = getOrders();
  const myOrders = orders.filter(o => o.userId === currentUser.id);
  const pending = myOrders.filter(o => ['pending','accepted','picked_up','delivering'].includes(o.status)).length;
  const completed = myOrders.filter(o => ['delivered','rated'].includes(o.status)).length;

  document.getElementById('view-userHome').innerHTML = `
    <div class="section-header">
      <div>
        <h2>👋 你好，${currentUser.name}</h2>
        <p>今天需要代取快递吗？</p>
      </div>
    </div>

    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-icon blue">📦</div>
        <div><div class="stat-value">${pending}</div><div class="stat-label">配送中订单</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon green">✅</div>
        <div><div class="stat-value">${completed}</div><div class="stat-label">已完成订单</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon orange">💰</div>
        <div><div class="stat-value">¥${myOrders.reduce((s,o) => s + o.price, 0)}</div><div class="stat-label">累计消费</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon purple">⭐</div>
        <div><div class="stat-value">${myOrders.filter(o => o.rating > 0).length}</div><div class="stat-label">已评价</div></div>
      </div>
    </div>

    <div class="quick-actions">
      <div class="quick-action" onclick="showCreateOrderForm()">
        <div class="qa-icon">📦</div>
        <div><h4>发布代取订单</h4><p>填写取件信息和送达地址</p></div>
      </div>
      <div class="quick-action" onclick="switchView('userOrders')">
        <div class="qa-icon">📋</div>
        <div><h4>查看我的订单</h4><p>追踪订单状态、评价跑腿</p></div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <h3>📋 最近订单</h3>
        <button class="btn btn-outline btn-sm" onclick="switchView('userOrders')">查看全部 →</button>
      </div>
      ${renderOrderList(myOrders.slice(0, 5), 'user')}
    </div>
  `;
}

function showCreateOrderForm() {
  const pickupOptions = PICKUP_POINTS.map((p, i) => `<option value="${i}">${p.name}</option>`).join('');
  const body = `
    <div class="form-row">
      <div class="form-group">
        <label>取件快递点 *</label>
        <select id="coPickup">${pickupOptions}</select>
      </div>
      <div class="form-group">
        <label>送达地址 *</label>
        <select id="coDelivery">
          ${DELIVERY_POINTS.map(p => `<option>${p}</option>`).join('')}
        </select>
      </div>
    </div>
    <div class="form-row">
      <div class="form-group">
        <label>包裹类型 *</label>
        <select id="coType">
          <option value="小件">小件（文件/小包裹）</option>
          <option value="中件">中件（鞋盒大小）</option>
          <option value="大件">大件（需要搬运）</option>
          <option value="急件">🔥 急件（优先处理）</option>
        </select>
      </div>
      <div class="form-group">
        <label>包裹重量</label>
        <input type="text" id="coWeight" placeholder="例如：2.5kg" value="1.0kg">
      </div>
    </div>
    <div class="form-group">
      <label>包裹描述</label>
      <input type="text" id="coDesc" placeholder="简要描述包裹内容" maxlength="50">
    </div>
    <div class="form-group">
      <label>收件人电话</label>
      <input type="text" id="coPhone" placeholder="方便跑腿员联系您" maxlength="11" value="${currentUser.phone}">
    </div>
    <div class="form-row">
      <div class="form-group">
        <label>期望配送费 (¥) *</label>
        <input type="number" id="coPrice" placeholder="建议5-15元" min="3" max="50" value="5">
      </div>
      <div class="form-group">
        <label>截止时间</label>
        <input type="datetime-local" id="coDeadline" value="${new Date(Date.now()+8*3600000).toISOString().slice(0,16)}">
      </div>
    </div>
    <div class="form-group">
      <label>备注</label>
      <textarea id="coNotes" placeholder="特殊要求：如放门口、需本人签收、先电话联系等" maxlength="100"></textarea>
    </div>
    <div class="error-msg" id="coError"></div>
  `;
  const actions = `
    <button class="btn btn-outline" onclick="closeModal()">取消</button>
    <button class="btn btn-primary btn-lg" onclick="submitOrder()">📦 发布订单</button>
  `;
  openModal('📦 发布代取订单', body, actions);
}

function submitOrder() {
  const pickupIdx = parseInt(document.getElementById('coPickup').value);
  const delivery = document.getElementById('coDelivery').value;
  const pkgType = document.getElementById('coType').value;
  const weight = document.getElementById('coWeight').value || '1.0kg';
  const desc = document.getElementById('coDesc').value.trim();
  const phone = document.getElementById('coPhone').value.trim();
  const price = parseInt(document.getElementById('coPrice').value) || 5;
  const deadline = document.getElementById('coDeadline').value.replace('T', ' ') || '';
  const notes = document.getElementById('coNotes').value.trim();
  const errEl = document.getElementById('coError');

  if (isNaN(pickupIdx)) { errEl.style.display='block'; errEl.textContent='请选择取件点'; return; }
  if (!delivery) { errEl.style.display='block'; errEl.textContent='请选择送达地址'; return; }
  if (!desc) { errEl.style.display='block'; errEl.textContent='请描述包裹内容'; return; }
  if (price < 3 || price > 50) { errEl.style.display='block'; errEl.textContent='配送费建议在3-50元之间'; return; }

  const pickup = PICKUP_POINTS[pickupIdx];
  const fee = Math.round(price * getConfig().platformFeeRate / 100 * 10) / 10;

  const order = {
    id: genOrderId(),
    userId: currentUser.id,
    runnerId: null,
    pickup: pickup.name,
    pickupDetail: pickup.detail,
    delivery: delivery,
    deliveryDetail: delivery,
    recipient: currentUser.name,
    recipientPhone: phone || currentUser.phone,
    packageType: pkgType,
    packageDesc: desc,
    weight: weight,
    price: price,
    fee: fee,
    status: 'pending',
    notes: notes,
    deadline: deadline,
    createdAt: now(),
    acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null,
    rating: 0, ratingComment: '',
  };

  const orders = getOrders();
  orders.unshift(order);
  saveOrders(orders);

  closeModal();
  showToast('✅ 订单发布成功！等待跑腿员接单');
  renderUserHome();
}

// ============================================================
// VIEW: USER ORDERS
// ============================================================
let userOrderFilter = 'all';
let userOrderSearch = '';
function renderUserOrders() {
  const orders = getOrders().filter(o => o.userId === currentUser.id);
  document.getElementById('view-userOrders').innerHTML = `
    <div class="section-header">
      <div><h2>📋 我的订单</h2><p>共 ${orders.length} 个订单</p></div>
      <button class="btn btn-primary btn-sm" onclick="showCreateOrderForm()">📦 发布新订单</button>
    </div>
    ${renderSearchBar('🔍 搜索订单号/取件点/送达点...', 'userOrderSearchInput', 'onUserOrderSearch')}
    <div class="tabs" id="userOrderTabs">
      <button class="tab ${userOrderFilter==='all'?'active':''}" data-filter="all">全部</button>
      <button class="tab ${userOrderFilter==='active'?'active':''}" data-filter="active">进行中</button>
      <button class="tab ${userOrderFilter==='done'?'active':''}" data-filter="done">已完成</button>
    </div>
    <div class="order-list" id="userOrderList"></div>
  `;

  document.querySelectorAll('#userOrderTabs .tab').forEach(tab => {
    tab.onclick = function() {
      userOrderFilter = this.dataset.filter;
      renderUserOrders();
    };
  });

  document.getElementById('userOrderList').onclick = function(e) {
    const card = e.target.closest('.order-card');
    if (card) showUserOrderDetail(card.dataset.orderId);
  };

  // Restore search input value
  setTimeout(() => {
    const inp = document.getElementById('userOrderSearchInput');
    if (inp) inp.value = userOrderSearch;
  }, 10);

  filterUserOrders(userOrderFilter);
}
function onUserOrderSearch(val) {
  userOrderSearch = val;
  filterUserOrders(userOrderFilter);
}
function filterUserOrders(filter) {
  userOrderFilter = filter;
  const orders = getOrders().filter(o => o.userId === currentUser.id);
  let result = orders;
  if (filter === 'active') result = orders.filter(o => !['delivered','rated'].includes(o.status));
  if (filter === 'done') result = orders.filter(o => ['delivered','rated'].includes(o.status));
  result = filterByKeyword(result, userOrderSearch, ['id','pickup','delivery','packageDesc','recipient']);
  document.getElementById('userOrderList').innerHTML = renderOrderList(result, 'user');
}

function showUserOrderDetail(orderId) {
  const order = getOrders().find(o => o.id === orderId);
  if (!order) return;

  const body = `
    ${renderOrderTimeline(order)}
    <div class="detail-row"><span class="detail-label">订单号</span><span class="detail-value">#${order.id.slice(-8)}</span></div>
    <div class="detail-row"><span class="detail-label">状态</span><span class="detail-value"><span class="tag tag-${order.status}">${statusLabel(order.status)}</span></span></div>
    <div class="detail-row"><span class="detail-label">取件点</span><span class="detail-value">${order.pickupDetail}</span></div>
    <div class="detail-row"><span class="detail-label">送达地址</span><span class="detail-value">${order.deliveryDetail}</span></div>
    <div class="detail-row"><span class="detail-label">包裹</span><span class="detail-value">${order.packageType} — ${order.packageDesc}</span></div>
    <div class="detail-row"><span class="detail-label">配送费</span><span class="detail-value" style="font-size:18px;color:var(--primary);font-weight:700;">¥${order.price}</span></div>
    ${order.runnerId ? `<div class="detail-row"><span class="detail-label">跑腿员</span><span class="detail-value">${getRunnerName(order.runnerId)}</span></div>` : ''}
    ${order.rating > 0 ? `<div class="detail-row"><span class="detail-label">我的评价</span><span class="detail-value">${renderStars(order.rating, false)} ${order.ratingComment}</span></div>` : ''}
  `;

  let actions = '<button class="btn btn-outline" onclick="closeModal()">关闭</button>';
  if (order.status === 'pending') {
    actions += `<button class="btn btn-danger btn-sm" onclick="userCancelOrder('${order.id}')">❌ 取消订单</button>`;
  }
  if (order.status === 'delivered') {
    actions += `<button class="btn btn-warning" onclick="showRatingForm('${order.id}')">⭐ 评价跑腿</button>`;
  }
  openModal('📋 订单详情 #' + order.id.slice(-8), body, actions);
}

function userCancelOrder(orderId) {
  if (!confirm('确定要取消该订单吗？')) return;
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (order && order.status === 'pending') {
    order.status = 'delivered';
    order.deliveredAt = now();
    saveOrders(orders);
  }
  closeModal();
  showToast('❌ 订单已取消');
  renderUserOrders();
}

function renderOrderTimeline(order) {
  const si = statusIdx(order.status);
  const steps = ['pending','accepted','picked_up','delivering','delivered'];
  const labels = ['待接单','已接单','已取件','配送中','已送达'];
  let html = '<div class="timeline">';
  steps.forEach((s, i) => {
    let cls = '';
    if (i < si) cls = 'done';
    else if (i === si && order.status !== 'rated') cls = 'current';
    if (order.status === 'rated') cls = 'done';
    const icon = i < si || order.status === 'rated' ? '✓' : (i === si ? '●' : '');
    html += `<div class="timeline-step">
      <div class="timeline-dot ${cls}">${icon}</div>
      <div class="timeline-label">${labels[i]}</div>
    </div>`;
  });
  html += '</div>';
  return html;
}

function showRatingForm(orderId) {
  const body = `
    <p style="margin-bottom:12px;">请为本次跑腿服务打分：</p>
    ${renderStars(5, true)}
    <div class="form-group" style="margin-top:16px;">
      <label>评价留言（选填）</label>
      <textarea id="ratingComment" placeholder="分享您的体验..." maxlength="100"></textarea>
    </div>
  `;
  const actions = `
    <button class="btn btn-outline" onclick="closeModal()">取消</button>
    <button class="btn btn-warning" onclick="submitRating('${orderId}')">⭐ 提交评价</button>
  `;
  openModal('⭐ 评价订单', body, actions);
}

function submitRating(orderId) {
  const starsEl = document.querySelector('.stars');
  const rating = parseInt(starsEl ? starsEl.getAttribute('data-rating') : 5);
  const comment = document.getElementById('ratingComment') ? document.getElementById('ratingComment').value.trim() : '';

  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (order) {
    order.rating = rating;
    order.ratingComment = comment;
    order.status = 'rated';
    saveOrders(orders);
  }
  closeModal();
  showToast('⭐ 评价成功！感谢您的反馈');
  renderUserOrders();
}

// ============================================================
// VIEW: RUNNER HALL (Order Pool)
// ============================================================
let hallSortBy = 'time';
let hallSearch = '';
function renderRunnerHall() {
  const orders = getOrders().filter(o => o.status === 'pending');
  const existingSort = document.getElementById('hallSort');
  if (existingSort) hallSortBy = existingSort.value;

  document.getElementById('view-runnerHall').innerHTML = `
    <div class="section-header">
      <div>
        <h2>📋 订单大厅</h2>
        <p>当前 ${orders.length} 个待接订单</p>
      </div>
      <div style="display:flex;gap:8px;">
        <select id="hallSort" onchange="renderRunnerHall()" style="padding:8px 12px;border-radius:8px;border:1px solid var(--border);font-size:13px;">
          <option value="time" ${hallSortBy==='time'?'selected':''}>按时间排序</option>
          <option value="price" ${hallSortBy==='price'?'selected':''}>按价格排序</option>
          <option value="urgent" ${hallSortBy==='urgent'?'selected':''}>急件优先</option>
        </select>
        <button class="btn btn-outline btn-sm" onclick="renderRunnerHall()">🔄 刷新</button>
      </div>
    </div>
    ${renderSearchBar('🔍 搜索订单号/取件点/送达点...', 'hallSearchInput', 'onHallSearch')}
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon blue">📋</div><div><div class="stat-value">${orders.length}</div><div class="stat-label">可接订单</div></div></div>
      <div class="stat-card"><div class="stat-icon green">💰</div><div><div class="stat-value">¥${orders.reduce((s,o)=>s+o.price,0)}</div><div class="stat-label">总配送费</div></div></div>
      <div class="stat-card"><div class="stat-icon red">🔥</div><div><div class="stat-value">${orders.filter(o=>o.packageType==='急件').length}</div><div class="stat-label">急件订单</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">⭐</div><div><div class="stat-value">4.9</div><div class="stat-label">我的好评率</div></div></div>
    </div>
    <div class="order-list" id="hallOrderList"></div>
  `;

  setTimeout(() => { const inp = document.getElementById('hallSearchInput'); if (inp) inp.value = hallSearch; }, 10);

  let sorted = filterByKeyword([...orders], hallSearch, ['id','pickup','delivery','packageDesc','recipient']);
  if (hallSortBy === 'price') sorted.sort((a,b) => b.price - a.price);
  if (hallSortBy === 'urgent') sorted.sort((a,b) => (b.packageType==='急件'?1:0) - (a.packageType==='急件'?1:0) || b.price - a.price);

  document.getElementById('hallOrderList').innerHTML = renderOrderList(sorted, 'runner');
  document.getElementById('hallOrderList').onclick = function(e) {
    const card = e.target.closest('.order-card');
    if (card) showRunnerOrderDetail(card.dataset.orderId);
  };
}
function onHallSearch(val) { hallSearch = val; renderRunnerHall(); }

function renderOrderList(orders, context) {
  if (!orders.length) {
    const msgs = {
      user_all: '还没有订单，快去发布第一个吧~',
      user_active: '没有进行中的订单',
      user_done: '还没有完成的订单',
      runner: '暂无可接订单，休息一下吧~',
      runner_my: '没有配送中的订单',
      admin: '暂无订单',
    };
    return `<div class="empty-state"><div class="icon">📭</div><h3>${msgs[context] || '暂无数据'}</h3></div>`;
  }
  return orders.map(o => {
    const tagClass = o.packageType === '急件' ? 'tag-urgent' : 'tag-' + o.status;
    return `
    <div class="order-card ${o.packageType === '急件' && o.status==='pending' ? 'pulse' : ''}" data-order-id="${o.id}">
      <div class="order-card-header">
        <div>
          <span class="order-id">#${o.id.slice(-8)}</span>
          <span class="order-time" style="margin-left:10px;">${formatTime(o.createdAt)}</span>
        </div>
        <span class="tag ${tagClass}">${o.packageType==='急件'?'🔥 急件':statusLabel(o.status)}</span>
      </div>
      <div class="order-route">
        <span class="route-dot from"></span>
        <span class="route-text" title="${o.pickup}">${o.pickup}</span>
        <span class="route-line"></span>
        <span class="route-dot to"></span>
        <span class="route-text" title="${o.delivery}">${o.delivery}</span>
      </div>
      <div class="order-footer">
        <div class="order-price">¥${o.price} <small style="font-size:12px;color:var(--text-secondary);">到手 ¥${(o.price - o.fee).toFixed(1)}</small></div>
        <div class="order-meta">
          <span>📦 ${o.packageType}</span>
          <span>⚖ ${o.weight}</span>
          <span>⏰ ${o.deadline ? o.deadline.slice(5) : ''}</span>
          ${context === 'admin' ? `<span>👤 ${getRunnerName(o.runnerId)}</span>` : ''}
        </div>
      </div>
    </div>`;
  }).join('');
}

function showRunnerOrderDetail(orderId) {
  const order = getOrders().find(o => o.id === orderId);
  if (!order) return;
  const body = `
    <div class="detail-row"><span class="detail-label">状态</span><span class="detail-value"><span class="tag tag-${order.status}">${statusLabel(order.status)}</span></span></div>
    <div class="detail-row"><span class="detail-label">包裹</span><span class="detail-value">${order.packageType} — ${order.packageDesc} (${order.weight})</span></div>
    <div class="detail-row"><span class="detail-label">取件点</span><span class="detail-value">${order.pickupDetail}</span></div>
    <div class="detail-row"><span class="detail-label">送达地址</span><span class="detail-value">${order.deliveryDetail}</span></div>
    <div class="detail-row"><span class="detail-label">收件人</span><span class="detail-value">${order.recipient} (${order.recipientPhone})</span></div>
    <div class="detail-row"><span class="detail-label">截止时间</span><span class="detail-value">${order.deadline || '-'}</span></div>
    <div class="detail-row"><span class="detail-label">备注</span><span class="detail-value">${order.notes || '无'}</span></div>
    <div class="detail-row"><span class="detail-label">配送费</span><span class="detail-value" style="font-size:18px;color:var(--primary);font-weight:700;">¥${order.price} <small style="color:var(--text-secondary);">(到手¥${(order.price-order.fee).toFixed(1)})</small></span></div>
  `;
  let actions = '<button class="btn btn-outline" onclick="closeModal()">关闭</button>';
  if (order.status === 'pending') {
    actions += `<button class="btn btn-primary" onclick="acceptOrder('${order.id}')">✅ 接单</button>`;
  }
  if (order.runnerId === currentUser.id && order.status === 'accepted') {
    actions += `<button class="btn btn-warning" onclick="pickupOrder('${order.id}')">📦 确认取件</button>`;
  }
  if (order.runnerId === currentUser.id && order.status === 'picked_up') {
    actions += `<button class="btn btn-primary" onclick="startDelivery('${order.id}')">🚀 开始配送</button>`;
  }
  if (order.runnerId === currentUser.id && order.status === 'delivering') {
    actions += `<button class="btn btn-success" onclick="completeDelivery('${order.id}')">✅ 确认送达</button>`;
  }
  openModal('📋 订单详情 #' + order.id.slice(-8), body, actions);
}

function acceptOrder(orderId) {
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (!order || order.status !== 'pending') return;
  order.runnerId = currentUser.id;
  order.status = 'accepted';
  order.acceptedAt = now();
  saveOrders(orders);
  closeModal();
  showToast('✅ 接单成功！请尽快前往取件');
  renderRunnerHall();
}

// ============================================================
// VIEW: RUNNER DELIVERIES
// ============================================================
let deliveryFilter = 'active';
let deliverySearch = '';
function renderRunnerDeliveries() {
  const orders = getOrders().filter(o => o.runnerId === currentUser.id);
  const active = orders.filter(o => !['delivered','rated'].includes(o.status));
  const done = orders.filter(o => ['delivered','rated'].includes(o.status));

  document.getElementById('view-runnerDeliveries').innerHTML = `
    <div class="section-header">
      <div><h2>🏃 我的配送</h2><p>${active.length} 个进行中，${done.length} 个已完成</p></div>
    </div>
    ${renderSearchBar('🔍 搜索订单号/取件点/送达点...', 'deliverySearchInput', 'onDeliverySearch')}
    <div class="tabs" id="runnerDeliveryTabs">
      <button class="tab ${deliveryFilter==='active'?'active':''}" data-filter="active">🔄 进行中 (${active.length})</button>
      <button class="tab ${deliveryFilter==='done'?'active':''}" data-filter="done">✅ 已完成 (${done.length})</button>
    </div>
    <div class="order-list" id="runnerDeliveryList"></div>
  `;

  setTimeout(() => { const inp = document.getElementById('deliverySearchInput'); if (inp) inp.value = deliverySearch; }, 10);

  document.querySelectorAll('#runnerDeliveryTabs .tab').forEach(tab => {
    tab.onclick = function() {
      deliveryFilter = this.dataset.filter;
      renderRunnerDeliveries();
    };
  });

  const list = filterByKeyword(deliveryFilter === 'active' ? active : done, deliverySearch, ['id','pickup','delivery','packageDesc','recipient']);
  document.getElementById('runnerDeliveryList').innerHTML = renderOrderList(list, 'runner_my');
  document.getElementById('runnerDeliveryList').onclick = function(e) {
    const card = e.target.closest('.order-card');
    if (card) showRunnerOrderDetail(card.dataset.orderId);
  };
}
function onDeliverySearch(val) { deliverySearch = val; renderRunnerDeliveries(); }

function pickupOrder(orderId) {
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (!order) return;
  order.status = 'picked_up';
  order.pickedUpAt = now();
  saveOrders(orders);
  closeModal();
  showToast('📦 已确认取件，请尽快配送');
  renderRunnerDeliveries();
}
function startDelivery(orderId) {
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (!order) return;
  order.status = 'delivering';
  order.deliveringAt = now();
  saveOrders(orders);
  closeModal();
  showToast('🚀 开始配送！');
  renderRunnerDeliveries();
}
function completeDelivery(orderId) {
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (!order) return;
  order.status = 'delivered';
  order.deliveredAt = now();
  saveOrders(orders);
  closeModal();
  showToast('✅ 已确认送达！辛苦了~');
  renderRunnerDeliveries();
}

// ============================================================
// VIEW: RUNNER EARNINGS
// ============================================================
function renderRunnerEarnings() {
  const orders = getOrders().filter(o => o.runnerId === currentUser.id);
  const doneOrders = orders.filter(o => ['delivered','rated'].includes(o.status));
  const totalEarnings = doneOrders.reduce((s, o) => s + (o.price - o.fee), 0);
  const totalOrders = doneOrders.length;
  const avgRating = doneOrders.filter(o => o.rating > 0).reduce((s,o,i,arr) => s + o.rating/arr.length, 0);

  // Today's earnings
  const today = new Date().toISOString().slice(0, 10);
  const todayOrders = doneOrders.filter(o => (o.deliveredAt || '').startsWith(today));
  const todayEarnings = todayOrders.reduce((s, o) => s + (o.price - o.fee), 0);

  // Weekly bar chart data
  const days = [];
  for (let i = 6; i >= 0; i--) {
    const d = new Date(Date.now() - i * 86400000);
    const ds = d.toISOString().slice(0, 10);
    const dayOrders = doneOrders.filter(o => (o.deliveredAt || '').startsWith(ds));
    days.push({
      label: ['日','一','二','三','四','五','六'][d.getDay()],
      date: ds.slice(5),
      amount: dayOrders.reduce((s, o) => s + (o.price - o.fee), 0),
      count: dayOrders.length,
    });
  }
  const maxAmount = Math.max(...days.map(d => d.amount), 1);

  document.getElementById('view-runnerEarnings').innerHTML = `
    <div class="section-header"><div><h2>💰 收入看板</h2><p>辛苦啦！这是你的劳动成果</p></div></div>
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon green">💰</div><div><div class="stat-value">¥${todayEarnings.toFixed(0)}</div><div class="stat-label">今日收入</div></div></div>
      <div class="stat-card"><div class="stat-icon blue">📊</div><div><div class="stat-value">¥${totalEarnings.toFixed(0)}</div><div class="stat-label">累计收入</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">📦</div><div><div class="stat-value">${totalOrders}</div><div class="stat-label">累计完成</div></div></div>
      <div class="stat-card"><div class="stat-icon purple">⭐</div><div><div class="stat-value">${avgRating.toFixed(1)}</div><div class="stat-label">平均评分</div></div></div>
    </div>

    <div class="card" style="margin-bottom:16px;">
      <h3 style="margin-bottom:16px;">📈 近7天收入</h3>
      <div class="earnings-bar">
        ${days.map(d => `
          <div class="bar-col">
            <div class="bar-value">¥${d.amount.toFixed(0)}</div>
            <div class="bar" style="height:${(d.amount/maxAmount*120) || 4}px;" title="${d.date}: ¥${d.amount.toFixed(1)} (${d.count}单)"></div>
            <div class="bar-label">${d.label}<br>${d.date}</div>
          </div>
        `).join('')}
      </div>
    </div>

    <div class="card">
      <h3 style="margin-bottom:12px;">📋 完成记录</h3>
      <div class="table-wrap">
        <table>
          <thead><tr><th>订单号</th><th>取件点</th><th>送达点</th><th>收入</th><th>评分</th><th>完成时间</th></tr></thead>
          <tbody>
            ${doneOrders.slice(-10).reverse().map(o => `
              <tr>
                <td style="font-weight:600;">#${o.id.slice(-8)}</td>
                <td>${o.pickup}</td>
                <td>${o.delivery}</td>
                <td style="color:var(--primary);font-weight:600;">¥${(o.price-o.fee).toFixed(1)}</td>
                <td>${o.rating > 0 ? '⭐'.repeat(o.rating) : '-'}</td>
                <td style="color:var(--text-secondary);">${formatTime(o.deliveredAt)}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    </div>
  `;
}

// ============================================================
// VIEW: ADMIN DASHBOARD
// ============================================================
function renderAdminDashboard() {
  const users = getUsers();
  const orders = getOrders();
  const runners = users.filter(u => u.role === 'runner');
  const normalUsers = users.filter(u => u.role === 'user');
  const pendingOrders = orders.filter(o => o.status === 'pending').length;
  const activeOrders = orders.filter(o => !['delivered','rated'].includes(o.status)).length;
  const completedOrders = orders.filter(o => ['delivered','rated'].includes(o.status)).length;
  const totalRevenue = orders.reduce((s, o) => s + o.fee, 0);

  document.getElementById('view-adminDashboard').innerHTML = `
    <div class="section-header"><div><h2>📊 管理仪表盘</h2><p>平台运营数据总览</p></div></div>
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon blue">👥</div><div><div class="stat-value">${normalUsers.length}</div><div class="stat-label">注册用户</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">🏃</div><div><div class="stat-value">${runners.length}</div><div class="stat-label">跑腿员</div></div></div>
      <div class="stat-card"><div class="stat-icon green">📦</div><div><div class="stat-value">${orders.length}</div><div class="stat-label">总订单</div></div></div>
      <div class="stat-card"><div class="stat-icon red">💰</div><div><div class="stat-value">¥${totalRevenue.toFixed(0)}</div><div class="stat-label">平台抽成</div></div></div>
    </div>
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon blue">📋</div><div><div class="stat-value">${pendingOrders}</div><div class="stat-label">待接订单</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">🔄</div><div><div class="stat-value">${activeOrders}</div><div class="stat-label">进行中订单</div></div></div>
      <div class="stat-card"><div class="stat-icon green">✅</div><div><div class="stat-value">${completedOrders}</div><div class="stat-label">已完成订单</div></div></div>
      <div class="stat-card"><div class="stat-icon purple">📊</div><div><div class="stat-value">${orders.length ? (completedOrders/orders.length*100).toFixed(0) : 0}%</div><div class="stat-label">完成率</div></div></div>
    </div>
    <div class="card">
      <div class="card-header"><h3>📋 最新订单</h3><button class="btn btn-outline btn-sm" onclick="switchView('adminOrders')">查看全部 →</button></div>
      ${renderOrderList(orders.slice(0, 8), 'admin')}
    </div>
  `;

  // Wire clicks
  document.querySelector('#view-adminDashboard .order-list').addEventListener('click', function(e) {
    const card = e.target.closest('.order-card');
    if (card) showAdminOrderDetail(card.dataset.orderId);
  });
}

// ============================================================
// VIEW: ADMIN USERS
// ============================================================
function renderAdminUsers() {
  const users = getUsers();
  document.getElementById('view-adminUsers').innerHTML = `
    <div class="section-header"><div><h2>👥 用户管理</h2><p>共 ${users.length} 个用户</p></div></div>
    <div class="card"><div class="table-wrap">
      <table>
        <thead><tr><th>姓名</th><th>手机号</th><th>角色</th><th>状态</th><th>注册时间</th><th>操作</th></tr></thead>
        <tbody>
          ${users.map(u => `
            <tr>
              <td style="font-weight:600;">${u.name}</td>
              <td>${u.phone}</td>
              <td><span class="tag ${u.role==='admin'?'tag-urgent':u.role==='runner'?'tag-accepted':'tag-pending'}">${{user:'用户',runner:'跑腿',admin:'管理'}[u.role]}</span></td>
              <td><span class="badge-dot ${u.status==='active'?'online':'offline'}"></span>${u.status==='active'?'正常':'禁用'}</td>
              <td style="color:var(--text-secondary);">${u.createdAt}</td>
              <td>
                ${u.role !== 'admin' ? `
                <button class="btn btn-xs ${u.status==='active'?'btn-danger':'btn-success'}" onclick="toggleUserStatus('${u.id}')">
                  ${u.status==='active'?'禁用':'启用'}
                </button>
                ` : '<span style="color:var(--text-muted);font-size:11px;">-</span>'}
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    </div></div>
  `;
}

function toggleUserStatus(userId) {
  const users = getUsers();
  const user = users.find(u => u.id === userId);
  if (!user || user.role === 'admin') return;
  user.status = user.status === 'active' ? 'disabled' : 'active';
  saveUsers(users);
  showToast(user.status === 'active' ? '✅ 用户已启用' : '🚫 用户已禁用');
  renderAdminUsers();
}

// ============================================================
// VIEW: ADMIN ORDERS
// ============================================================
let adminOrderFilter = 'all';
let adminOrderSearch = '';
function renderAdminOrders() {
  const orders = getOrders();
  document.getElementById('view-adminOrders').innerHTML = `
    <div class="section-header"><div><h2>📦 订单管理</h2><p>共 ${orders.length} 个订单</p></div></div>
    ${renderSearchBar('🔍 搜索订单号/取件点/送达点/用户名...', 'adminOrderSearchInput', 'onAdminOrderSearch')}
    <div class="tabs" id="adminOrderTabs">
      <button class="tab ${adminOrderFilter==='all'?'active':''}" data-filter="all">全部</button>
      <button class="tab ${adminOrderFilter==='pending'?'active':''}" data-filter="pending">待接单</button>
      <button class="tab ${adminOrderFilter==='active'?'active':''}" data-filter="active">进行中</button>
      <button class="tab ${adminOrderFilter==='done'?'active':''}" data-filter="done">已完成</button>
    </div>
    <div class="order-list" id="adminOrderList"></div>
  `;

  setTimeout(() => { const inp = document.getElementById('adminOrderSearchInput'); if (inp) inp.value = adminOrderSearch; }, 10);

  document.querySelectorAll('#adminOrderTabs .tab').forEach(tab => {
    tab.onclick = function() {
      adminOrderFilter = this.dataset.filter;
      renderAdminOrders();
    };
  });
  filterAdminOrders(adminOrderFilter);
  document.getElementById('adminOrderList').onclick = function(e) {
    const card = e.target.closest('.order-card');
    if (card) showAdminOrderDetail(card.dataset.orderId);
  };
}
function onAdminOrderSearch(val) { adminOrderSearch = val; filterAdminOrders(adminOrderFilter); }
function filterAdminOrders(filter) {
  adminOrderFilter = filter;
  let orders = getOrders();
  if (filter === 'pending') orders = orders.filter(o => o.status === 'pending');
  if (filter === 'active') orders = orders.filter(o => !['delivered','rated'].includes(o.status));
  if (filter === 'done') orders = orders.filter(o => ['delivered','rated'].includes(o.status));
  orders = filterByKeyword(orders, adminOrderSearch, ['id','pickup','delivery','packageDesc','recipient', 'userId', 'runnerId']);
  document.getElementById('adminOrderList').innerHTML = renderOrderList(orders, 'admin');
}

function showAdminOrderDetail(orderId) {
  const order = getOrders().find(o => o.id === orderId);
  if (!order) return;
  const user = getUsers().find(u => u.id === order.userId);
  const body = `
    ${renderOrderTimeline(order)}
    <div class="detail-row"><span class="detail-label">订单号</span><span class="detail-value">${order.id}</span></div>
    <div class="detail-row"><span class="detail-label">状态</span><span class="detail-value"><span class="tag tag-${order.status}">${statusLabel(order.status)}</span></span></div>
    <div class="detail-row"><span class="detail-label">下单用户</span><span class="detail-value">${user ? user.name : '未知'} (${user ? user.phone : ''})</span></div>
    <div class="detail-row"><span class="detail-label">跑腿员</span><span class="detail-value">${getRunnerName(order.runnerId)}</span></div>
    <div class="detail-row"><span class="detail-label">取件点</span><span class="detail-value">${order.pickupDetail}</span></div>
    <div class="detail-row"><span class="detail-label">送达地址</span><span class="detail-value">${order.deliveryDetail}</span></div>
    <div class="detail-row"><span class="detail-label">包裹</span><span class="detail-value">${order.packageType} — ${order.packageDesc}</span></div>
    <div class="detail-row"><span class="detail-label">配送费/抽成</span><span class="detail-value">¥${order.price} / ¥${order.fee.toFixed(1)}</span></div>
    <div class="detail-row"><span class="detail-label">截止时间</span><span class="detail-value">${order.deadline || '-'}</span></div>
    <div class="detail-row"><span class="detail-label">创建时间</span><span class="detail-value">${order.createdAt}</span></div>
  `;
  let actions = '<button class="btn btn-outline" onclick="closeModal()">关闭</button>';
  if (['pending','accepted','picked_up','delivering'].includes(order.status)) {
    actions += `<button class="btn btn-danger btn-sm" onclick="adminCancelOrder('${order.id}')">⚠ 强制取消</button>`;
  }
  openModal('📋 订单管理 #' + order.id.slice(-8), body, actions);
}

function adminCancelOrder(orderId) {
  if (!confirm('确定要强制取消该订单吗？此操作不可恢复。')) return;
  const orders = getOrders();
  const order = orders.find(o => o.id === orderId);
  if (order) {
    order.status = 'delivered'; // mark as ended
    order.deliveredAt = now();
    saveOrders(orders);
  }
  closeModal();
  showToast('⚠ 订单已被管理员取消');
  renderAdminOrders();
}

// ============================================================
// VIEW: ADMIN RUNNERS
// ============================================================
function renderAdminRunners() {
  const runners = getUsers().filter(u => u.role === 'runner');
  const applicants = getUsers().filter(u => u.role === 'user' && u.runnerApplication && u.runnerApplication.status === 'pending');

  document.getElementById('view-adminRunners').innerHTML = `
    <div class="section-header"><div><h2>🏃 跑腿员管理</h2><p>${runners.length} 个跑腿员，${applicants.length} 个待审核</p></div></div>
    ${applicants.length ? `
    <div class="card" style="border:2px solid var(--warning);margin-bottom:16px;">
      <h3 style="margin-bottom:12px;color:var(--warning);">⏳ 待审核申请 (${applicants.length})</h3>
      <div class="table-wrap"><table>
        <thead><tr><th>姓名</th><th>学号</th><th>手机号</th><th>申请时间</th><th>申请理由</th><th>操作</th></tr></thead>
        <tbody>${applicants.map(a => `
          <tr>
            <td style="font-weight:600;">${a.name}</td>
            <td>${a.studentId}</td>
            <td>${a.phone || '-'}</td>
            <td>${a.runnerApplication.appliedAt}</td>
            <td>${a.runnerApplication.reason || '无'}</td>
            <td>
              <button class="btn btn-xs btn-success" onclick="approveRunner('${a.id}')">✅ 通过</button>
              <button class="btn btn-xs btn-danger" style="margin-left:4px;" onclick="rejectRunner('${a.id}')">❌ 拒绝</button>
            </td>
          </tr>`).join('')}</tbody>
      </table></div>
    </div>` : ''}
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon blue">🏃</div><div><div class="stat-value">${runners.length}</div><div class="stat-label">跑腿员总数</div></div></div>
      <div class="stat-card"><div class="stat-icon green">✅</div><div><div class="stat-value">${runners.filter(r=>r.status==='active').length}</div><div class="stat-label">在岗</div></div></div>
      <div class="stat-card"><div class="stat-icon red">🚫</div><div><div class="stat-value">${runners.filter(r=>r.status!=='active').length}</div><div class="stat-label">已禁用</div></div></div>
      <div class="stat-card"><div class="stat-icon purple">📦</div><div><div class="stat-value">${getOrders().filter(o=>o.runnerId&&['delivered','rated'].includes(o.status)).length}</div><div class="stat-label">总完成订单</div></div></div>
    </div>
    <div class="card"><div class="table-wrap">
      <table>
        <thead><tr><th>姓名</th><th>学号</th><th>状态</th><th>完成订单</th><th>好评率</th><th>余额</th><th>注册时间</th><th>操作</th></tr></thead>
        <tbody>
          ${runners.map(r => {
            const doneOrders = getOrders().filter(o => o.runnerId === r.id && ['delivered','rated'].includes(o.status));
            const earnings = doneOrders.reduce((s,o) => s + (o.price - o.fee), 0);
            const ratedCount = doneOrders.filter(o => o.rating > 0).length;
            const avgRating = ratedCount > 0 ? (doneOrders.filter(o=>o.rating>0).reduce((s,o)=>s+o.rating,0) / ratedCount).toFixed(1) : '-';
            return `
            <tr>
              <td style="font-weight:600;">${r.name}</td>
              <td>${r.studentId}</td>
              <td><span class="badge-dot ${r.status==='active'?'online':'offline'}"></span>${r.status==='active'?'在岗':'禁用'}</td>
              <td>${doneOrders.length}</td>
              <td>${avgRating} ${avgRating!=='-'?'⭐':''}</td>
              <td style="color:var(--primary);font-weight:600;">¥${r.balance.toFixed(0)}</td>
              <td style="color:var(--text-secondary);">${r.createdAt}</td>
              <td>
                <button class="btn btn-xs ${r.status==='active'?'btn-danger':'btn-success'}" onclick="toggleUserStatus('${r.id}');renderAdminRunners();">
                  ${r.status==='active'?'禁用':'启用'}
                </button>
              </td>
            </tr>`;
          }).join('')}
        </tbody>
      </table>
    </div></div>
  `;
}

function approveRunner(userId) {
  const users = getUsers();
  const user = users.find(u => u.id === userId);
  if (!user) return;
  user.role = 'runner';
  user.runnerApplication.status = 'approved';
  user.runnerApplication.reviewedAt = new Date().toISOString().slice(0, 10);
  user.runnerApplication.reviewedBy = currentUser.id;
  saveUsers(users);
  showToast('✅ 已通过 ' + user.name + ' 的跑腿申请');
  renderAdminRunners();
}
function rejectRunner(userId) {
  const users = getUsers();
  const user = users.find(u => u.id === userId);
  if (!user) return;
  user.runnerApplication.status = 'rejected';
  user.runnerApplication.reviewedAt = new Date().toISOString().slice(0, 10);
  user.runnerApplication.reviewedBy = currentUser.id;
  saveUsers(users);
  showToast('❌ 已拒绝申请');
  renderAdminRunners();
}

// ============================================================
// VIEW: USER - APPLY FOR RUNNER
// ============================================================
function renderRunnerApply() {
  const u = currentUser;
  const existing = u.runnerApplication;
  document.getElementById('view-runnerApply').innerHTML = `
    <div class="section-header"><div><h2>🎯 申请成为跑腿员</h2><p>利用课余时间赚取零花钱</p></div></div>
    <div class="card" style="max-width:600px;">
      ${existing && existing.status === 'rejected' ? '<div class="error-msg" style="display:block;margin-bottom:16px;">⚠ 您之前的申请已被拒绝，可以重新申请</div>' : ''}
      <div class="form-group"><label>学号</label><input type="text" value="${u.studentId}" disabled style="background:#F3F4F6;"></div>
      <div class="form-group"><label>姓名</label><input type="text" value="${u.name}" disabled style="background:#F3F4F6;"></div>
      <div class="form-group"><label>手机号</label><input type="text" id="applyPhone" value="${u.phone || ''}" placeholder="请输入手机号"></div>
      <div class="form-group"><label>申请理由</label><textarea id="applyReason" placeholder="请简要说明：如课余时间、可配送时段等"></textarea></div>
      <div style="padding:12px;background:#FFF7ED;border-radius:8px;font-size:12px;color:#92400E;margin-bottom:12px;">
        💡 提交后将由管理员审核，审核通过后即可开始接单配送。
      </div>
      <div class="error-msg" id="applyError"></div>
      <button class="btn btn-primary btn-block btn-lg" onclick="submitRunnerApply()">📩 提交申请</button>
    </div>
  `;
}
function submitRunnerApply() {
  const reason = document.getElementById('applyReason').value.trim();
  const phone = document.getElementById('applyPhone').value.trim();
  const errEl = document.getElementById('applyError');
  if (!phone) { errEl.style.display='block'; errEl.textContent='请输入手机号'; return; }
  // Update phone
  const users = getUsers();
  const user = users.find(u => u.id === currentUser.id);
  user.phone = phone;
  saveUsers(users);
  currentUser.phone = phone;
  const result = applyForRunner(reason);
  if (!result.ok) { errEl.style.display='block'; errEl.textContent=result.msg; return; }
  showToast('✅ ' + result.msg);
  goHome();
}

// ============================================================
// VIEW: RUNNER WITHDRAW
// ============================================================
function renderRunnerWithdraw() {
  const withdrawals = getWithdrawals().filter(w => w.runnerId === currentUser.id);
  const cfg = getConfig();
  document.getElementById('view-runnerWithdraw').innerHTML = `
    <div class="section-header"><div><h2>💳 收益提现</h2><p>可提现余额：<b style="color:var(--primary);font-size:18px;">¥${currentUser.balance.toFixed(0)}</b></p></div></div>
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon green">💰</div><div><div class="stat-value">¥${currentUser.balance.toFixed(0)}</div><div class="stat-label">可提现余额</div></div></div>
      <div class="stat-card"><div class="stat-icon blue">📊</div><div><div class="stat-value">¥${currentUser.totalEarned.toFixed(0)}</div><div class="stat-label">累计收入</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">💳</div><div><div class="stat-value">¥${currentUser.totalWithdrawn.toFixed(0)}</div><div class="stat-label">已提现</div></div></div>
      <div class="stat-card"><div class="stat-icon purple">🔒</div><div><div class="stat-value">¥${cfg.minWithdraw}</div><div class="stat-label">最低提现额</div></div></div>
    </div>
    <div class="card" style="max-width:500px;margin-bottom:16px;">
      <h3 style="margin-bottom:12px;">📤 申请提现</h3>
      <div class="form-row">
        <div class="form-group"><label>提现金额 (¥)</label><input type="number" id="withdrawAmount" placeholder="最低 ¥${cfg.minWithdraw}" min="${cfg.minWithdraw}" max="${currentUser.balance}"></div>
      </div>
      <div class="error-msg" id="withdrawError"></div>
      <button class="btn btn-warning btn-block" onclick="submitWithdraw()" ${currentUser.balance < cfg.minWithdraw ? 'disabled' : ''}>
        💳 申请提现（最低 ¥${cfg.minWithdraw}）
      </button>
    </div>
    <div class="card">
      <h3 style="margin-bottom:12px;">📋 提现记录</h3>
      ${withdrawals.length === 0 ? '<div class="empty-state"><div class="icon">📭</div><h3>暂无提现记录</h3></div>' : `
      <div class="table-wrap"><table>
        <thead><tr><th>金额</th><th>状态</th><th>申请时间</th><th>处理时间</th><th>备注</th></tr></thead>
        <tbody>${withdrawals.reverse().map(w => `
          <tr>
            <td style="font-weight:600;color:var(--primary);">¥${w.amount}</td>
            <td><span class="tag ${w.status==='completed'?'tag-delivered':w.status==='pending'?'tag-accepted':'tag-urgent'}">${{pending:'处理中',completed:'已到账',rejected:'已退回'}[w.status]}</span></td>
            <td>${w.requestedAt}</td>
            <td>${w.processedAt || '-'}</td>
            <td>${w.note || '-'}</td>
          </tr>`).join('')}</tbody>
      </table></div>`}
    </div>
  `;
}
function submitWithdraw() {
  const amount = parseInt(document.getElementById('withdrawAmount').value) || 0;
  const errEl = document.getElementById('withdrawError');
  const result = requestWithdrawal(amount);
  if (!result.ok) { errEl.style.display='block'; errEl.textContent=result.msg; return; }
  showToast('✅ ' + result.msg);
  renderRunnerWithdraw();
}

// ============================================================
// VIEW: ADMIN VIOLATIONS
// ============================================================
function renderAdminViolations() {
  const violations = getViolations();
  const orders = getOrders();
  document.getElementById('view-adminViolations').innerHTML = `
    <div class="section-header"><div><h2>⚠ 违规订单管控</h2><p>共 ${violations.length} 条违规记录</p></div></div>
    <div class="card" style="margin-bottom:16px;border:2px solid var(--danger);">
      <h3 style="margin-bottom:12px;color:var(--danger);">🚨 新增违规</h3>
      <div class="form-row">
        <div class="form-group"><label>订单号</label><input type="text" id="vioOrderId" placeholder="例如：DD20260706001"></div>
        <div class="form-group"><label>处理方式</label>
          <select id="vioAction"><option value="warning">⚠ 警告</option><option value="cancel">❌ 强制取消</option><option value="fine">💰 罚款</option></select>
        </div>
      </div>
      <div class="form-group"><label>违规原因</label><textarea id="vioReason" placeholder="请填写违规原因..."></textarea></div>
      <div class="error-msg" id="vioError"></div>
      <button class="btn btn-danger" onclick="submitViolation()">⚠ 提交违规处理</button>
    </div>
    <div class="card">
      <h3 style="margin-bottom:12px;">📋 违规记录</h3>
      ${violations.length === 0 ? '<div class="empty-state"><div class="icon">✅</div><h3>暂无违规记录</h3></div>' : `
      <div class="table-wrap"><table>
        <thead><tr><th>订单号</th><th>原因</th><th>处理方式</th><th>处理人</th><th>时间</th></tr></thead>
        <tbody>${violations.reverse().map(v => `
          <tr>
            <td style="font-weight:600;">#${v.orderId.slice(-8)}</td>
            <td>${v.reason}</td>
            <td><span class="tag ${v.action==='cancel'?'tag-urgent':v.action==='fine'?'tag-accepted':'tag-pending'}">${{warning:'⚠ 警告',cancel:'❌ 取消',fine:'💰 罚款'}[v.action]}</span></td>
            <td>${getUserName(v.reportedBy)}</td>
            <td style="color:var(--text-secondary);">${v.createdAt}</td>
          </tr>`).join('')}</tbody>
      </table></div>`}
    </div>
  `;
}
function submitViolation() {
  const orderId = document.getElementById('vioOrderId').value.trim();
  const reason = document.getElementById('vioReason').value.trim();
  const action = document.getElementById('vioAction').value;
  const errEl = document.getElementById('vioError');
  if (!orderId) { errEl.style.display='block'; errEl.textContent='请输入订单号'; return; }
  if (!reason) { errEl.style.display='block'; errEl.textContent='请填写违规原因'; return; }
  const order = getOrders().find(o => o.id === orderId);
  if (!order) { errEl.style.display='block'; errEl.textContent='订单不存在'; return; }
  reportViolation(orderId, reason, action);
  showToast('⚠ 违规处理已提交');
  renderAdminViolations();
}

// ============================================================
// VIEW: ADMIN SETTINGS
// ============================================================
function renderAdminSettings() {
  const cfg = getConfig();
  document.getElementById('view-adminSettings').innerHTML = `
    <div class="section-header"><div><h2>⚙ 平台设置</h2><p>管理平台运行参数</p></div></div>
    <div class="card" style="max-width:600px;">
      <h3 style="margin-bottom:16px;">💰 费率设置</h3>
      <div class="form-group"><label>平台抽成比例 (%)</label>
        <input type="number" id="setFeeRate" value="${cfg.platformFeeRate}" min="0" max="50" step="1">
        <span class="form-hint">当前：每单配送费的 ${cfg.platformFeeRate}% 作为平台服务费</span>
      </div>
      <div class="form-group"><label>最低提现金额 (¥)</label>
        <input type="number" id="setMinWithdraw" value="${cfg.minWithdraw}" min="1" max="500" step="1">
        <span class="form-hint">跑腿员余额达到此金额后方可申请提现</span>
      </div>
      <div class="error-msg" id="setError"></div>
      <button class="btn btn-primary" onclick="saveSettings()">💾 保存设置</button>
    </div>
    <div class="card" style="max-width:600px;margin-top:16px;">
      <h3 style="margin-bottom:12px;">🔄 数据管理</h3>
      <div style="display:flex;gap:8px;flex-wrap:wrap;">
        <button class="btn btn-outline btn-sm" onclick="if(confirm('确定重置所有数据？此操作不可恢复！')){localStorage.clear();location.reload();}">⚠ 重置全部数据</button>
        <button class="btn btn-outline btn-sm" onclick="exportData()">📥 导出数据</button>
      </div>
    </div>
  `;
}
function saveSettings() {
  const rate = parseInt(document.getElementById('setFeeRate').value);
  const minW = parseInt(document.getElementById('setMinWithdraw').value);
  if (isNaN(rate) || rate < 0 || rate > 50) { document.getElementById('setError').style.display='block'; document.getElementById('setError').textContent='费率需在0-50之间'; return; }
  saveConfig({ platformFeeRate: rate, minWithdraw: minW });
  showToast('✅ 设置已保存');
}
function exportData() {
  const data = { users: getUsers(), orders: getOrders(), withdrawals: getWithdrawals(), violations: getViolations(), config: getConfig() };
  const blob = new Blob([JSON.stringify(data, null, 2)], {type:'application/json'});
  const a = document.createElement('a'); a.href = URL.createObjectURL(blob); a.download = 'delivery_data_backup.json'; a.click();
  showToast('📥 数据已导出');
}

// ============================================================
// VIEW: ADMIN STATS (Enhanced)
// ============================================================
function renderAdminStats() {
  const orders = getOrders();
  const users = getUsers();
  const runners = users.filter(u => u.role === 'runner');
  const cfg = getConfig();
  const totalFee = orders.reduce((s,o) => s + o.fee, 0);
  const byStatus = {};
  STATUS_ORDER.forEach(s => { byStatus[s] = orders.filter(o => o.status === s).length; });
  const byType = {};
  ['小件','中件','大件','急件'].forEach(t => { byType[t] = orders.filter(o => o.packageType === t).length; });

  // Daily stats for last 7 days
  const dailyStats = [];
  for (let i = 6; i >= 0; i--) {
    const d = new Date(Date.now() - i*86400000);
    const ds = d.toISOString().slice(0,10);
    const dayOrders = orders.filter(o => (o.createdAt||'').startsWith(ds));
    dailyStats.push({ label: ['日','一','二','三','四','五','六'][d.getDay()], date: ds.slice(5), created: dayOrders.length, completed: dayOrders.filter(o=>['delivered','rated'].includes(o.status)).length, fee: dayOrders.reduce((s,o)=>s+o.fee,0) });
  }
  const maxCreated = Math.max(...dailyStats.map(d=>d.created), 1);

  document.getElementById('view-adminStats').innerHTML = `
    <div class="section-header"><div><h2>📈 订单数据统计</h2><p>平台运营数据分析</p></div></div>
    <div class="stats-row">
      <div class="stat-card"><div class="stat-icon blue">📦</div><div><div class="stat-value">${orders.length}</div><div class="stat-label">总订单数</div></div></div>
      <div class="stat-card"><div class="stat-icon green">✅</div><div><div class="stat-value">${orders.filter(o=>['delivered','rated'].includes(o.status)).length}</div><div class="stat-label">已完成</div></div></div>
      <div class="stat-card"><div class="stat-icon orange">⏳</div><div><div class="stat-value">${orders.filter(o=>!['delivered','rated'].includes(o.status)).length}</div><div class="stat-label">进行中</div></div></div>
      <div class="stat-card"><div class="stat-icon red">💰</div><div><div class="stat-value">¥${totalFee.toFixed(0)}</div><div class="stat-label">平台收入</div></div></div>
    </div>

    <div class="card" style="margin-bottom:16px;">
      <h3 style="margin-bottom:16px;">📊 近7天订单趋势</h3>
      <div class="earnings-bar">
        ${dailyStats.map(d => `
          <div class="bar-col">
            <div class="bar-value">${d.created}</div>
            <div class="bar" style="height:${(d.created/maxCreated*120)||4}px;background:var(--primary);" title="新建:${d.created} 完成:${d.completed}"></div>
            <div class="bar-label">${d.label}<br>${d.date}</div>
          </div>`).join('')}
      </div>
      <div style="display:flex;gap:16px;margin-top:8px;font-size:11px;color:var(--text-secondary);">
        <span>🟦 新建订单</span><span>🟢 完成: ${dailyStats.reduce((s,d)=>s+d.completed,0)}</span><span>💰 抽成: ¥${dailyStats.reduce((s,d)=>s+d.fee,0).toFixed(0)}</span>
      </div>
    </div>

    <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;margin-bottom:16px;">
      <div class="card">
        <h3 style="margin-bottom:12px;">📦 订单状态分布</h3>
        ${Object.entries(byStatus).map(([k,v]) => `
          <div style="display:flex;align-items:center;gap:8px;margin-bottom:8px;">
            <span style="font-size:12px;width:50px;">${statusLabel(k)}</span>
            <div style="flex:1;height:18px;background:var(--bg);border-radius:9px;overflow:hidden;">
              <div style="height:100%;width:${orders.length?(v/orders.length*100):0}%;background:var(--primary);border-radius:9px;transition:width .3s;"></div>
            </div>
            <span style="font-size:12px;font-weight:600;width:30px;">${v}</span>
          </div>`).join('')}
      </div>
      <div class="card">
        <h3 style="margin-bottom:12px;">📋 包裹类型分布</h3>
        ${Object.entries(byType).map(([k,v]) => `
          <div style="display:flex;align-items:center;gap:8px;margin-bottom:8px;">
            <span style="font-size:12px;width:50px;">${k}</span>
            <div style="flex:1;height:18px;background:var(--bg);border-radius:9px;overflow:hidden;">
              <div style="height:100%;width:${orders.length?(v/orders.length*100):0}%;background:${k==='急件'?'var(--danger)':'var(--success)'};border-radius:9px;"></div>
            </div>
            <span style="font-size:12px;font-weight:600;width:30px;">${v}</span>
          </div>`).join('')}
      </div>
    </div>

    <div class="card">
      <h3 style="margin-bottom:12px;">💡 关键指标</h3>
      <div class="stats-row" style="margin-bottom:0;">
        <div class="stat-card"><div class="stat-icon green">📈</div><div><div class="stat-value">${orders.length ? (orders.filter(o=>['delivered','rated'].includes(o.status)).length/orders.length*100).toFixed(1) : 0}%</div><div class="stat-label">订单完成率</div></div></div>
        <div class="stat-card"><div class="stat-icon blue">👥</div><div><div class="stat-value">${users.filter(u=>u.role==='user').length}</div><div class="stat-label">注册用户</div></div></div>
        <div class="stat-card"><div class="stat-icon orange">🏃</div><div><div class="stat-value">${runners.length}</div><div class="stat-label">跑腿员</div></div></div>
        <div class="stat-card"><div class="stat-icon red">💵</div><div><div class="stat-value">${cfg.platformFeeRate}%</div><div class="stat-label">平台费率</div></div></div>
      </div>
    </div>
  `;
}

