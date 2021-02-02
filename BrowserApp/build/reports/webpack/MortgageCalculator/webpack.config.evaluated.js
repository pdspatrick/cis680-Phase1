{
  mode: 'development',
  resolve: {
    modules: [
      'D:\\GitHub\\cis680-Phase1\\BrowserApp\\build\\js\\packages\\MortgageCalculator\\kotlin-dce-dev',
      'node_modules'
    ]
  },
  plugins: [
    TeamCityErrorPlugin {}
  ],
  module: {
    rules: [
      {
        test: /\.js$/,
        use: [
          'source-map-loader'
        ],
        enforce: 'pre'
      },
      {
        test: /\.css$/,
        use: [
          {
            loader: 'style-loader',
            options: {}
          },
          {
            loader: 'css-loader',
            options: {}
          }
        ]
      }
    ]
  },
  entry: {
    main: [
      'D:\\GitHub\\cis680-Phase1\\BrowserApp\\build\\js\\packages\\MortgageCalculator\\kotlin-dce-dev\\MortgageCalculator.js'
    ]
  },
  output: {
    path: 'D:\\GitHub\\cis680-Phase1\\BrowserApp\\build\\distributions',
    filename: [Function: filename],
    library: 'MortgageCalculator',
    libraryTarget: 'umd'
  },
  devtool: 'eval-source-map',
  stats: {
    warningsFilter: [
      /Failed to parse source map/
    ],
    warnings: false,
    errors: false
  },
  devServer: {
    inline: true,
    lazy: false,
    noInfo: true,
    open: true,
    overlay: false,
    contentBase: [
      'D:\\GitHub\\cis680-Phase1\\BrowserApp\\build\\processedResources\\js\\main'
    ]
  }
}