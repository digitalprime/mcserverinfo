
# Minecraft Server Info (Server Side Rendering)

This is the source to the website `www.mcserverinfo.com`. This is a pure `Hiccup` and Clojure implementation. Happy to correct bugs or major issues with the code and accept PR where appropriate. The site itself is up and running and is not really maintained. Yes this code uses a database for the site data and for obvious reasons the connection details have been removed from the source. 

## Why

Education really. I find that there is not a great deal of information on build web applications with Clojure and there is quite a few pitfalls for the newcomer. I fallen in love with Clojure a few years back and I wanted to give back a little to the community with a working example.

This was originally and `Vue.js` API application, with a static front end and the Clojure back end supplying the data. I ran in to the 'no content' problem with Google then I decided to rewrite it so it was one application rendering its own data. It also proved a good learning source for others as it was simple enough. I just hope it useful to at least someone. 

  
## Docker, docker-compose and Traefik

This is deployed in production on a Linux server using Docker and `docker-compose`. I have included the build for this and you can see the uberjar is build within docker itself and shoved into another layer to be executed. Its a clean setup. Also work mentioning I use `traefik` for the routing and you will see how that is setup as well.

    $ docker-compose build
    $ docker-compose push
    $ docker-compose up -d
    
also the usual

    $ lein ring uberjar

or for development

    $ lein ring server-headless

## License

MIT License.
