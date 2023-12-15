FROM node:20.9.0-alpine AS builder

WORKDIR /home/app

COPY /* /home/app

RUN npm install
RUN npm run build

#FROM nginx:latest
#RUN rm /etc/nginx/conf.d/default.conf
#RUN rm -rf /etc/nginx/conf.d/*
#COPY ./nginx.conf /etc/nginx/conf.d/default.conf
#
#COPY --from=builder /FE/build /usr/share/nginx/html
#EXPOSE 80
#CMD [ "nginx", "-g", "daemon off;"]