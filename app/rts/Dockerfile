FROM 20minutes/node-16-yarn as build

RUN apk add --no-cache bash

COPY ./rts /rts
COPY ./shared /shared

RUN ls -a

RUN cd /shared && yarn install
RUN cd /rts && sh ./build.sh



FROM node:16.14.0

LABEL maintainer="admin@localhost"


COPY --from=build /rts/package.json ./
COPY --from=build /rts/dist ./

EXPOSE 8091

CMD ["node", "--require", "source-map-support/register", "server.js"]
