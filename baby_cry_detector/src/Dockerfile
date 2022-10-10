# Docker image for "baby-cry-detection"

#Deriving the latest base image
FROM python:latest
#Labels as key value pair
LABEL Maintainer="nitu.gupta"

COPY requirements.txt requirements.txt
COPY . /src

# environment variables
ENV TARGET_DIR=/baby-cry-detection
ENV HTTP_PORT=56001
ENV APP_BIN_DIR=bin
ENV APP_NAME=baby-cry-detection

# copy files
RUN mkdir -p $TARGET_DIR/recording/

# install required tools
RUN pip3 install -r requirements.txt
RUN apt-get update -y && apt-get install -y --no-install-recommends build-essential gcc libsndfile1 

# expose ports
EXPOSE $HTTP_PORT

# set the working directory
WORKDIR $TARGET_DIR

# kick off the edge-orchestration container
CMD ["python", "/src/make_prediction.py"]
