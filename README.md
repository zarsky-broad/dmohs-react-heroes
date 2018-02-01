# dmohs-react-heroes

dmohs/react version of Angular's [Tour of Heroes](https://angular.io/tutorial) tutorial.

**[Live version here!](https://zarsky-broad.github.io/dmohs-react-heroes/)**

## Dependencies

[Leiningen](https://leiningen.org) and npm.

You can just `brew install leiningen` on a Mac.

## Running

```sh
$ lein figwheel
```
and in a new terminal
```sh
$ npm install
$ npm run webpack-watcher
```

Then open a browser (Chrome is best if you want to poke around in the console) to http://localhost:3449.

## Building

```sh
$ lein with-profile deploy cljsbuild once
$ NODE_ENV=production npm run webpack
```

The built files will be in `resources/public/`
