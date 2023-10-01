const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = '开发阶段端口映射器';
      return args;
    });
  },
  devServer: {
    host: "0.0.0.0",
    port: 9872,
    proxy: {
      "/api": {
        target: "http://localhost:9873",
        changeOrigin: true,
        pathRewrite: {
          '/api': '/'
        },
      }
    },
    allowedHosts: "all",
  },
  productionSourceMap: true,
  configureWebpack: {
    resolve: {
      alias: {},
    },
    devtool: "source-map",
  },
})