FROM node:16.14.0

LABEL maintainer="tech@appsmith.com"

WORKDIR /app

COPY ./rts/package.json ./rts/dist ./

EXPOSE 8091

CMD ["node", "--require", "source-map-support/register", "server.js"]
