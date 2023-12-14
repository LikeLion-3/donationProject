FROM node:20.9.0-alpine as build

WORKDIR /warmwave/FE

COPY package.json package-lock.json ./

RUN npm install

COPY . ./

EXPOSE 3000