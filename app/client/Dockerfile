# FROM 20minutes/node-16-yarn as build
#
# COPY ./client /client
# COPY ./shared /shared
# COPY ./rts /rts
#
# RUN cd /shared && yarn install
# RUN cd /rts && yarn install
# RUN cd /client && yarn global add @craco/craco  && yarn install && sh ./build.sh


FROM nginx:1.20-alpine

#COPY --from=build /client/build /var/www/appsmith

COPY ./client/build /var/www/appsmith

EXPOSE 80

ENV APPSMITH_SERVER_PROXY_PASS="http://appsmith-internal-server:8080"
ENV APPSMITH_RTS_PROXY_PASS="http://appsmith-rts:8091"

# This is the default nginx template file inside the container.
# This is replaced by the install.sh script during a deployment
# COPY --from=build /client/docker/templates/nginx-root.conf.template /nginx-root.conf.template

# COPY --from=build /client/docker/templates/nginx-app-http.conf.template /nginx-app-http.conf.template
# COPY --from=build /client/docker/templates/nginx-app-https.conf.template /nginx-app-https.conf.template

COPY ./client/docker/templates/nginx-root.conf.template /nginx-root.conf.template

COPY ./client/docker/templates/nginx-app-http.conf.template /nginx-app-http.conf.template
COPY ./client/docker/templates/nginx-app-https.conf.template /nginx-app-https.conf.template

# This is the script that is used to start Nginx when the Docker container starts
# COPY --from=build /client/docker/start-nginx.sh /start-nginx.sh
# COPY --from=build /client/start-https.sh /start-https.sh

COPY ./client/docker/start-nginx.sh /start-nginx.sh
COPY ./client/start-https.sh /start-https.sh
HEALTHCHECK --interval=15s --timeout=15s --start-period=15s --retries=3 CMD curl -Lfk http://localhost/ || exit 1
CMD ["/start-nginx.sh"]
