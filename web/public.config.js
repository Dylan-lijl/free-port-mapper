const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          javascriptEnabled: true,
        },
      },
    },
  },
})