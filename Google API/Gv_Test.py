from base64 import b64encode
from os import makedirs
from os.path import join, basename
from sys import argv
import json
import requests

ENDPOINT_URL = 'https://vision.googleapis.com/v1/images:annotate'
RESULTS_DIR = 'jsons'
makedirs(RESULTS_DIR, exist_ok=True)

def make_image_data_list(image_filenames):
    """
    image_filenames is a list of filename strings
    Returns a list of dicts formatted as the Vision API
        needs them to be
    """
    img_requests = []
    for imgname in image_filenames:
        with open(imgname, 'rb') as f:
            ctxt = b64encode(f.read()).decode()
            img_requests.append({
                    'image': {'content': ctxt},
                    'features': [{
                        'type': 'TEXT_DETECTION',
                        'maxResults': 1
                    }]
            })
    return img_requests

def make_image_data(image_filenames):
    """Returns the image data lists as bytes"""
    imgdict = make_image_data_list(image_filenames)
    return json.dumps({"requests": imgdict }).encode()


def request_ocr(api_key, image_filenames):
    response = requests.post(ENDPOINT_URL,
                             data=make_image_data(image_filenames),
                             params={'key': api_key},
                             headers={'Content-Type': 'application/json'})
    return response

def hasRandom(word):
    if len(word) == 0 or word.isspace():
        return False
    letters = 'qwertyuiopasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLMNBVCX'
    numbers = '1234567890'
    random = '@#$%^&*()+_=\|]}[{;:>,</.!?}"'
    word_ = word[0:len(word)-1]
    if len(word_) > 0:
        prev_letter = word_[0]
    for letter in word_:
        if letter in random:
            return True
        if (prev_letter in letters and letter in numbers) or (prev_letter in numbers and letter in letters):
            return True
        prev_letter = letter
    # . ! ? are ok at end as punctuation
    if word[-1] in random[0:24]:
        return True
    return False

def hasLetter(word):
    letters = 'qwertyuiopasdfghjklmnbvcxzQWERTYUIOPASDFGHJKLMNBVCX1234567890'
    for letter in word:
        if letter not in letters:
            return False
    return True

def removeNonAscii(s): return "".join(i for i in s if ord(i)<128)

# Creates global dictionary variable
with open("dict.txt") as word_file:
    english_words = set(word.strip().lower() for word in word_file)

def is_word(word):
    return word.lower() in english_words

if __name__ == '__main__':
    ## api_key, *image_filenames = argv[1:]
    api_key = 'AIzaSyC15tABjgjnGRO4pshXCrH7ONlgTgGXWHk'
    image_filenames = ['Images/13.jpg']

    if not api_key or not image_filenames:
        print("""
            Please supply an api key, then one or more image filenames

            $ python cloudvisreq.py api_key image1.jpg image2.png""")
    else:
        response = request_ocr(api_key, image_filenames)
        if response.status_code != 200 or response.json().get('error'):
            print(response.text)
        else:
            for idx, resp in enumerate(response.json()['responses']):
                # save to JSON file
                imgname = image_filenames[idx]
                jpath = join(RESULTS_DIR, basename(imgname) + '.json')
                with open(jpath, 'w') as f:
                    datatxt = json.dumps(resp, indent=2)
                    print("Wrote", len(datatxt), "bytes to", jpath)
                    f.write(datatxt)
                t = resp['textAnnotations'][0]
                result = t['description']
                result = removeNonAscii(result)
                print("Original Sample:")
                print(result)
                result = result.replace("\n", " ")
                result = result.split(" ")
                result = [word for word in result if not hasRandom(word)]
                result = [word for word in result if is_word(word)]
                result = ' '.join(result)
                print()
                print("Sanitized Sample:")
                print(result)
                print()
                print()
