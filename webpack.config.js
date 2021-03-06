const path = require('path');

const webpack = require('webpack');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const definePlugin = new webpack.DefinePlugin({
    'process.env': {
        NODE_ENV: JSON.stringify(process.env.NODE_ENV) // to make sure it's parseable
    }
});
const plugins = [definePlugin, new CopyWebpackPlugin(['src/static'])];

if (process.env.NODE_ENV === 'production') {
    plugins.push(new UglifyJSPlugin());
}

module.exports = {
    entry: "./src/webpack-requires.js",
    output: {
        path: path.join(__dirname, "resources/public/"),
        filename: 'webpack-deps.js'
    },
    plugins: plugins,
    watchOptions: {
        ignored: /node_modules/
    }
};
