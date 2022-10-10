# -*- coding: utf-8 -*-

import argparse
import os
import pickle
import sys
import warnings
import sys
import logging
import time
from pprint import pprint
sys.path.insert(0, 'home/nitup/HOMEEDGE/2022/Demo_Android/baby_cry_detector')
pprint(sys.path)

from method import Reader
from method.baby_cry_predictor import BabyCryPredictor
from method.feature_engineer import FeatureEngineer
from method.majority_voter import MajorityVoter


def main():

   
    parser = argparse.ArgumentParser()
    parser.add_argument('--load_path_data',
                        default='{}/../baby-cry-detection/recording/'.format(os.path.dirname(os.path.abspath(__file__))))
    parser.add_argument('--load_path_model',
                        default='{}/../src/model/'.format(os.path.dirname(os.path.abspath(__file__))))
    parser.add_argument('--save_path',
                        default='{}/../src/prediction/'.format(os.path.dirname(os.path.abspath(__file__))))

    # Arguments
    args = parser.parse_args()
    load_path_data = os.path.normpath(args.load_path_data)
    load_path_model = os.path.normpath(args.load_path_model)
    save_path = os.path.normpath(args.save_path)
    
    #############Logging
    logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
    handlers=[
        logging.FileHandler("debug.log"),
        logging.StreamHandler(sys.stdout)
    ]
)

    #Let us Create an object 
    logger=logging.getLogger() 
    logger.info("Starting to detect if baby is crying")

    ####################################################################################################################
    # READ RAW SIGNAL
    ####################################################################################################################

    # Read signal
    file_name = 'sample.wav'       # only one file in the folder
    
    file_reader = Reader(os.path.join(load_path_data, file_name))
    #file_reader1 = Reader("/baby-cry-detection/recording/cry_2.wav")
    #pprint(file_reader1)
    play_list = file_reader.read_audio_file()
    #play_list1 = file_reader1.read_audio_file()
    #pprint(play_list1)

    ####################################################################################################################
    # iteration
    ####################################################################################################################

    # iterate on play_list for feature engineering and prediction

    ####################################################################################################################
    # FEATURE ENGINEERING
    ####################################################################################################################

    # Feature extraction
    engineer = FeatureEngineer()

    play_list_processed = list()

    for signal in play_list:
        tmp = engineer.feature_engineer(signal)
        play_list_processed.append(tmp)

    ####################################################################################################################
    # MAKE PREDICTION
    ####################################################################################################################

    # https://stackoverflow.com/questions/41146759/check-sklearn-version-before-loading-model-using-joblib
    with warnings.catch_warnings():
      warnings.simplefilter("ignore", category=UserWarning)

      with open((os.path.join(load_path_model, 'model.pkl')), 'rb') as fp:
          model = pickle.load(fp)

    predictor = BabyCryPredictor(model)

    predictions = list()

    for signal in play_list_processed:
        tmp = predictor.classify(signal)
        predictions.append(tmp)

    ####################################################################################################################
    # MAJORITY VOTE
    ####################################################################################################################

    majority_voter = MajorityVoter(predictions)
    majority_vote = majority_voter.vote()

    if majority_vote == 1:
        logger.info("Baby is crying!!!!!")
    else:
        logger.info("Baby is not crying!!!!")
    ####################################################################################################################
    # SAVE
    ####################################################################################################################
    # Save prediction result
    with open(os.path.join(save_path, 'prediction.txt'), 'w') as text_file:
        text_file.write("{0}".format(majority_vote))

if __name__ == '__main__':
    main()
