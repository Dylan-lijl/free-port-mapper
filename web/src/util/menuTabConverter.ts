export function toTab(menu) {
  return { title: menu.label, key: menu.key, closable: !!(menu.closable), icon: menu.icon };
}

export function toMenu(tab) {
  return { label: tab.title, key: tab.key, closable: !!(tab.closable), icon: tab.icon };
}

export default {
  toTab,
  toMenu
}