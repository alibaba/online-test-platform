const path = require('path');

module.exports = {
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
      'umi': path.resolve(__dirname, 'umi'),
    },
  }
};
