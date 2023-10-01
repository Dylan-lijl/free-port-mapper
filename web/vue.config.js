const { defineConfig } = require('@vue/cli-service')
const publicConfig = require("./public.config");
//确定环境
const envConfig = process.env.NODE_ENV === 'production' ? require("./prod.config") : require("./dev.config")
module.exports = defineConfig({
  transpileDependencies: true,
  ...publicConfig,
  ...envConfig
})
