'use strict';

// ============================================================
// DATA LAYER
// ============================================================
const LS = {
  get(key) { try { return JSON.parse(localStorage.getItem(key)); } catch(e) { return null; } },
  set(key, val) { localStorage.setItem(key, JSON.stringify(val)); },
};

function loadUsers() { return LS.get('dlv_users') || []; }
function saveUsers(u) { LS.set('dlv_users', u); }
function loadOrders() { return LS.get('dlv_orders') || []; }
function saveOrders(o) { LS.set('dlv_orders', o); }
function loadSession() { return LS.get('dlv_session') || null; }
function saveSession(s) { LS.set('dlv_session', s); }
function loadWithdrawals() { return LS.get('dlv_withdrawals') || []; }
function saveWithdrawals(w) { LS.set('dlv_withdrawals', w); }
function loadViolations() { return LS.get('dlv_violations') || []; }
function saveViolations(v) { LS.set('dlv_violations', v); }
function loadConfig() { return LS.get('dlv_config') || { platformFeeRate: 20, minWithdraw: 10 }; }
function saveConfig(c) { LS.set('dlv_config', c); }

// ============================================================
// SEED DATA
// ============================================================
const PICKUP_POINTS = [
  { name: '快递服务中心', detail: '学校正门西侧 快递服务中心大厅 2号窗口' },
  { name: '一期综合楼快递站', detail: '一期综合楼一层 快递驿站 B区' },
];

const DELIVERY_POINTS = [
  '国光1 101室', '国光1 203室', '国光2 305室', '国光3 112室', '国光4 408室',
  '国光5 201室', '国光6 310室', '国光7 105室', '国光8 502室', '国光9 207室',
  '国光10 313室', '国光11 109室', '国光12 404室', '国光13 206室', '国光14 501室', '国光15 308室',
  '芙蓉1 102室', '芙蓉2 205室', '芙蓉3 310室', '芙蓉4 108室', '芙蓉5 403室',
  '芙蓉6 201室', '芙蓉7 304室', '芙蓉8 107室',
];

const SEED_USERS = [
  { id: 'u1', name: '张三', phone: '13800000001', password: '123456', role: 'user', avatar: '张', status: 'active', createdAt: '2026-06-01', studentId: '2024001001', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: null },
  { id: 'u2', name: '李四', phone: '13800000002', password: '123456', role: 'user', avatar: '李', status: 'active', createdAt: '2026-06-02', studentId: '2024001002', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: null },
  { id: 'u3', name: '王五', phone: '13800000003', password: '123456', role: 'user', avatar: '王', status: 'active', createdAt: '2026-06-03', studentId: '2024001003', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: { status: 'pending', appliedAt: '2026-07-05', reason: '课余时间多，想赚点生活费' } },
  { id: 'r1', name: '李小明', phone: '13900000001', password: '123456', role: 'runner', avatar: '李', status: 'active', createdAt: '2026-06-01', studentId: '2023002001', balance: 28, totalEarned: 35, totalWithdrawn: 7, runnerApplication: { status: 'approved', appliedAt: '2026-05-20', reviewedAt: '2026-05-21', reviewedBy: 'a1' } },
  { id: 'r2', name: '王大强', phone: '13900000002', password: '123456', role: 'runner', avatar: '王', status: 'active', createdAt: '2026-06-05', studentId: '2023002002', balance: 14, totalEarned: 14, totalWithdrawn: 0, runnerApplication: { status: 'approved', appliedAt: '2026-06-01', reviewedAt: '2026-06-02', reviewedBy: 'a1' } },
  { id: 'r3', name: '陈飞', phone: '13900000003', password: '123456', role: 'runner', avatar: '陈', status: 'active', createdAt: '2026-06-10', studentId: '2023002003', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: { status: 'approved', appliedAt: '2026-06-08', reviewedAt: '2026-06-09', reviewedBy: 'a1' } },
  { id: 'u4', name: '赵六', phone: '13800000004', password: '123456', role: 'user', avatar: '赵', status: 'active', createdAt: '2026-07-01', studentId: '2024002001', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: null },
  { id: 'a1', name: '管理员', phone: '18800000001', password: 'admin', role: 'admin', avatar: '管', status: 'active', createdAt: '2026-05-01', studentId: 'admin001', balance: 0, totalEarned: 0, totalWithdrawn: 0, runnerApplication: null },
];

const SEED_ORDERS = [
  { id: 'DD20260706001', userId: 'u1', runnerId: null, pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅 2号窗口', delivery: '国光3', deliveryDetail: '国光3 112室', recipient: '张三', recipientPhone: '138****6789', packageType: '小件', packageDesc: '数码配件（手机壳）', weight: '0.3kg', price: 5, fee: 1.0, status: 'pending', notes: '放门口即可', deadline: '2026-07-06 18:00', createdAt: '2026-07-06 09:30', acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260706002', userId: 'u2', runnerId: null, pickup: '一期综合楼快递站', pickupDetail: '一期综合楼一层 快递驿站 B区', delivery: '芙蓉2', deliveryDetail: '芙蓉2 205室', recipient: '李四', recipientPhone: '139****2345', packageType: '中件', packageDesc: '书籍（3本）', weight: '2.5kg', price: 8, fee: 1.6, status: 'pending', notes: '需本人签收', deadline: '2026-07-06 20:00', createdAt: '2026-07-06 10:00', acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260706003', userId: 'u3', runnerId: null, pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅 2号窗口', delivery: '国光8', deliveryDetail: '国光8 502室', recipient: '王老师', recipientPhone: '136****7890', packageType: '大件', packageDesc: '家用电器（加湿器）', weight: '4.2kg', price: 12, fee: 2.4, status: 'pending', notes: '先电话联系，下午3点后在家', deadline: '2026-07-06 17:00', createdAt: '2026-07-06 08:15', acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260706004', userId: 'u1', runnerId: null, pickup: '一期综合楼快递站', pickupDetail: '一期综合楼一层 快递驿站 B区', delivery: '芙蓉5', deliveryDetail: '芙蓉5 403室', recipient: '赵六', recipientPhone: '137****4567', packageType: '小件', packageDesc: '零食包裹', weight: '0.8kg', price: 5, fee: 1.0, status: 'pending', notes: '', deadline: '2026-07-06 19:00', createdAt: '2026-07-06 11:00', acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260706005', userId: 'u2', runnerId: null, pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅 2号窗口', delivery: '国光12', deliveryDetail: '国光12 404室', recipient: '钱七', recipientPhone: '135****8901', packageType: '急件', packageDesc: '药品（需冷藏）', weight: '0.5kg', price: 15, fee: 3.0, status: 'pending', notes: '急需！请尽快送达，冷藏保存', deadline: '2026-07-06 14:00', createdAt: '2026-07-06 12:00', acceptedAt: null, pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260705006', userId: 'u3', runnerId: 'r1', pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅', delivery: '国光1', deliveryDetail: '国光1 203室', recipient: '孙八', recipientPhone: '134****0123', packageType: '中件', packageDesc: '运动鞋一双', weight: '1.2kg', price: 7, fee: 1.4, status: 'accepted', notes: '', deadline: '2026-07-06 18:00', createdAt: '2026-07-05 16:00', acceptedAt: '2026-07-05 17:00', pickedUpAt: null, deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260705007', userId: 'u1', runnerId: 'r1', pickup: '一期综合楼快递站', pickupDetail: '一期综合楼一层 快递驿站 B区', delivery: '芙蓉1', deliveryDetail: '芙蓉1 102室', recipient: '周九', recipientPhone: '133****3456', packageType: '小件', packageDesc: '文件资料', weight: '0.2kg', price: 5, fee: 1.0, status: 'picked_up', notes: '放快递架即可', deadline: '2026-07-06 17:00', createdAt: '2026-07-05 14:00', acceptedAt: '2026-07-05 15:00', pickedUpAt: '2026-07-05 16:30', deliveringAt: null, deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260705008', userId: 'u2', runnerId: 'r2', pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅', delivery: '国光6', deliveryDetail: '国光6 310室', recipient: '留学生Tom', recipientPhone: '132****5678', packageType: '中件', packageDesc: '衣物包裹', weight: '1.8kg', price: 8, fee: 1.6, status: 'delivering', notes: '到之前发短信', deadline: '2026-07-06 16:00', createdAt: '2026-07-05 10:00', acceptedAt: '2026-07-05 11:00', pickedUpAt: '2026-07-05 13:00', deliveringAt: '2026-07-05 14:00', deliveredAt: null, rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260704009', userId: 'u1', runnerId: 'r1', pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅', delivery: '国光14', deliveryDetail: '国光14 501室', recipient: '吴十', recipientPhone: '131****7890', packageType: '小件', packageDesc: '日用品', weight: '0.6kg', price: 5, fee: 1.0, status: 'delivered', notes: '', deadline: '2026-07-04 18:00', createdAt: '2026-07-04 09:00', acceptedAt: '2026-07-04 10:00', pickedUpAt: '2026-07-04 11:00', deliveringAt: '2026-07-04 11:30', deliveredAt: '2026-07-04 12:10', rating: 0, ratingComment: '', violation: null },
  { id: 'DD20260704010', userId: 'u3', runnerId: 'r2', pickup: '一期综合楼快递站', pickupDetail: '一期综合楼一层 快递驿站 B区', delivery: '芙蓉8', deliveryDetail: '芙蓉8 107室', recipient: '郑十一', recipientPhone: '130****3456', packageType: '小件', packageDesc: '化妆品', weight: '0.4kg', price: 6, fee: 1.2, status: 'rated', notes: '', deadline: '2026-07-04 19:00', createdAt: '2026-07-04 08:00', acceptedAt: '2026-07-04 09:00', pickedUpAt: '2026-07-04 09:40', deliveringAt: '2026-07-04 10:00', deliveredAt: '2026-07-04 10:30', rating: 5, ratingComment: '送得很快，态度好！', violation: null },
  { id: 'DD20260703011', userId: 'u2', runnerId: 'r1', pickup: '快递服务中心', pickupDetail: '学校正门西侧 快递服务中心大厅', delivery: '国光15', deliveryDetail: '国光15 308室', recipient: '孙十二', recipientPhone: '129****5678', packageType: '大件', packageDesc: '论文资料（厚厚一摞）', weight: '5.0kg', price: 10, fee: 2.0, status: 'rated', notes: '请小心搬运，资料很重要', deadline: '2026-07-03 17:00', createdAt: '2026-07-03 10:00', acceptedAt: '2026-07-03 10:30', pickedUpAt: '2026-07-03 11:00', deliveringAt: '2026-07-03 11:20', deliveredAt: '2026-07-03 12:00', rating: 4, ratingComment: '还不错，稍微慢了一点', violation: null },
];

const SEED_WITHDRAWALS = [
  { id: 'w1', runnerId: 'r1', amount: 7, status: 'completed', requestedAt: '2026-07-01 10:00', processedAt: '2026-07-01 15:00', processedBy: 'a1', note: '微信到账' },
];

const SEED_VIOLATIONS = [];

function seedData() {
  if (!LS.get('dlv_seeded_v3')) {
    saveUsers(SEED_USERS);
    saveOrders(SEED_ORDERS);
    saveWithdrawals(SEED_WITHDRAWALS);
    saveViolations(SEED_VIOLATIONS);
    saveConfig({ platformFeeRate: 20, minWithdraw: 10 });
    LS.set('dlv_seeded_v3', true);
    // Migrate old data if exists
    LS.set('dlv_seeded', true);
  }
}

// ============================================================
// AUTH & SESSION
// ============================================================
let currentUser = null;

function getUsers() { return loadUsers(); }
function getOrders() { return loadOrders(); }
function getWithdrawals() { return loadWithdrawals(); }
function getViolations() { return loadViolations(); }
function getConfig() { return loadConfig(); }

function login(studentId, password) {
  const users = getUsers();
  const user = users.find(u => u.studentId === studentId && u.password === password);
  if (!user) return { ok: false, msg: '学号或密码错误' };
  if (user.status !== 'active') return { ok: false, msg: '账号已被禁用，请联系管理员' };
  currentUser = user;
  saveSession({ id: user.id, role: user.role });
  return { ok: true, user };
}

function register(name, studentId, phone, password, role) {
  const users = getUsers();
  if (users.find(u => u.studentId === studentId)) return { ok: false, msg: '该学号已注册' };
  if (phone && users.find(u => u.phone === phone)) return { ok: false, msg: '该手机号已注册' };
  // Runner applicants stay as 'user' until admin approves
  const isRunnerApply = role === 'runner';
  const runnerApp = isRunnerApply
    ? { status: 'pending', appliedAt: new Date().toISOString().slice(0, 10), reason: '' }
    : null;
  const newUser = {
    id: 'u' + Date.now(),
    name, phone: phone || '', password,
    role: isRunnerApply ? 'user' : role,  // runner applicants are 'user' until approved
    avatar: name.charAt(0),
    status: 'active',
    createdAt: new Date().toISOString().slice(0, 10),
    studentId,
    balance: 0, totalEarned: 0, totalWithdrawn: 0,
    runnerApplication: runnerApp,
  };
  users.push(newUser);
  saveUsers(users);
  currentUser = newUser;
  saveSession({ id: newUser.id, role: newUser.role });
  return { ok: true, user: newUser };
}

// Apply for runner (existing user)
function applyForRunner(reason) {
  if (!currentUser || currentUser.role !== 'user') return { ok: false, msg: '仅普通用户可申请' };
  if (currentUser.runnerApplication) {
    if (currentUser.runnerApplication.status === 'pending') return { ok: false, msg: '申请审核中，请耐心等待' };
    if (currentUser.runnerApplication.status === 'approved') return { ok: false, msg: '您已是跑腿员' };
  }
  const users = getUsers();
  const user = users.find(u => u.id === currentUser.id);
  user.runnerApplication = { status: 'pending', appliedAt: new Date().toISOString().slice(0, 10), reason: reason || '' };
  saveUsers(users);
  currentUser = user;
  return { ok: true, msg: '申请已提交，等待管理员审核' };
}

function logout() {
  currentUser = null;
  saveSession(null);
  switchView('login');
}

function restoreSession() {
  const session = loadSession();
  if (!session) return false;
  const users = getUsers();
  const user = users.find(u => u.id === session.id && u.role === session.role);
  if (!user || user.status !== 'active') { saveSession(null); return false; }
  currentUser = user;
  return true;
}

// ============================================================
// WITHDRAWAL
// ============================================================
function requestWithdrawal(amount) {
  if (!currentUser || currentUser.role !== 'runner') return { ok: false, msg: '仅跑腿员可提现' };
  const cfg = getConfig();
  if (amount < cfg.minWithdraw) return { ok: false, msg: '最低提现金额为 ¥' + cfg.minWithdraw };
  if (amount > currentUser.balance) return { ok: false, msg: '余额不足，当前余额 ¥' + currentUser.balance.toFixed(0) };
  const withdrawals = getWithdrawals();
  const w = {
    id: 'w' + Date.now(),
    runnerId: currentUser.id,
    amount: amount,
    status: 'pending',
    requestedAt: new Date().toISOString().slice(0, 16).replace('T', ' '),
    processedAt: null,
    processedBy: null,
    note: '',
  };
  withdrawals.push(w);
  saveWithdrawals(withdrawals);
  // Deduct from balance
  const users = getUsers();
  const user = users.find(u => u.id === currentUser.id);
  user.balance -= amount;
  user.totalWithdrawn += amount;
  saveUsers(users);
  currentUser = user;
  return { ok: true, msg: '提现申请已提交，等待管理员处理' };
}

function processWithdrawal(withdrawalId, action, adminId) {
  const withdrawals = getWithdrawals();
  const w = withdrawals.find(w => w.id === withdrawalId);
  if (!w) return { ok: false, msg: '记录不存在' };
  if (action === 'complete') {
    w.status = 'completed';
    w.processedAt = new Date().toISOString().slice(0, 16).replace('T', ' ');
    w.processedBy = adminId;
    w.note = '已打款';
  } else if (action === 'reject') {
    w.status = 'rejected';
    w.processedAt = new Date().toISOString().slice(0, 16).replace('T', ' ');
    w.processedBy = adminId;
    // Refund balance
    const users = getUsers();
    const user = users.find(u => u.id === w.runnerId);
    if (user) {
      user.balance += w.amount;
      user.totalWithdrawn -= w.amount;
      saveUsers(users);
      if (currentUser && currentUser.id === user.id) currentUser = user;
    }
  }
  saveWithdrawals(withdrawals);
  return { ok: true };
}

// ============================================================
// VIOLATION
// ============================================================
function reportViolation(orderId, reason, action) {
  const violations = getViolations();
  violations.push({
    id: 'v' + Date.now(),
    orderId: orderId,
    reportedBy: currentUser ? currentUser.id : 'system',
    reason: reason,
    action: action, // 'warning' | 'cancel' | 'fine'
    createdAt: new Date().toISOString().slice(0, 16).replace('T', ' '),
  });
  saveViolations(violations);

  if (action === 'cancel') {
    const orders = getOrders();
    const order = orders.find(o => o.id === orderId);
    if (order && !['delivered', 'rated'].includes(order.status)) {
      order.status = 'delivered';
      order.deliveredAt = new Date().toISOString().slice(0, 16).replace('T', ' ');
      order.violation = { reason, action, at: new Date().toISOString().slice(0, 16).replace('T', ' ') };
      saveOrders(orders);
    }
  }
  return { ok: true };
}

// ============================================================
// AUTO VIOLATION CHECK — 接单后24小时未送达自动违规
// ============================================================
function checkOverdueOrders() {
  const orders = getOrders();
  const violations = getViolations();
  const nowTime = new Date();
  let changed = false;

  orders.forEach(order => {
    if (['delivered', 'rated'].includes(order.status)) return;
    if (!order.acceptedAt) return;
    const acceptedTime = new Date(order.acceptedAt);
    const hoursSinceAccept = (nowTime - acceptedTime) / (1000 * 60 * 60);
    if (hoursSinceAccept > 24) {
      order.status = 'delivered';
      order.deliveredAt = nowTime.toISOString().slice(0, 16).replace('T', ' ');
      order.violation = {
        reason: '接单后24小时未送达，系统自动标记违规',
        action: 'cancel',
        at: nowTime.toISOString().slice(0, 16).replace('T', ' ')
      };
      violations.push({
        id: 'v' + Date.now() + Math.random().toString(36).slice(2,6),
        orderId: order.id,
        reportedBy: 'system',
        reason: '接单后24小时未送达（自动检测）',
        action: 'cancel',
        createdAt: nowTime.toISOString().slice(0, 16).replace('T', ' ')
      });
      changed = true;
    }
  });
  if (changed) {
    saveOrders(orders);
    saveViolations(violations);
  }
}
