// ============================================================
// INIT
// ============================================================
function init() {
  seedData();
  checkOverdueOrders();
  const restored = restoreSession();
  if (restored) {
    goHome();
  } else {
    switchView('login');
  }
}

init();
