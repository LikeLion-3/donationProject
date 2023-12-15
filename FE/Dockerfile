#FROM node:20.9.0-alpine AS builder
FROM nginx

WORKDIR /home/app

COPY package.json package-lock.json /
COPY ./* /

RUN npm install
RUN npm run build

FROM nginx:latest
RUN rm /etc/nginx/conf.d/default.conf
RUN rm -rf /etc/nginx/conf.d/*
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /home/app/build /usr/share/nginx/html
EXPOSE 80