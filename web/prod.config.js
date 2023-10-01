const { defineConfig } = require('@vue/cli-service')
const CompressionPlugin = require("compression-webpack-plugin");
module.exports = defineConfig({
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].title = '端口映射器';
      return args;
    });
  },
  productionSourceMap: false,
  pluginOptions: {
    gzipPlugin: new CompressionPlugin({
      test: /\.js$|\.html$|\.css/,//文件后缀
      threshold: 10240,//1024*10 kb
      deleteOriginalAssets: false,
    }),
  }
})