.PHONY: install release ui

install:
	npm install

release:
	rm -rf ./resources/public/js
	npx shadow-cljs release app

ui:
	npx shadow-cljs watch app
