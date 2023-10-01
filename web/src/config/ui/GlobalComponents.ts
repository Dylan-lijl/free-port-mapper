import { Input, Layout, Button, Menu, Tabs, Divider, Table, Dropdown, Modal, Form, Select, Tooltip, Switch, Spin } from "ant-design-vue";
import { App } from "vue";
const components = [Input, Layout, Button, Menu, Tabs, Divider, Table, Dropdown, Modal, Form, Select, Tooltip, Switch, Spin];
//注册antdv ui
export default function registerUI(app: App): void {
  for (const component of components) {
    app.use(component);
  }
}