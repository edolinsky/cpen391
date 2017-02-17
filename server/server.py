from flask import Flask
from flask import request
from flask import jsonify
import datetime
import requests
import json
import sys

app = Flask(__name__)

trusted_users = {'annalies': 'cpen',
                 'reid': 'cpen',
                 'omar': 'cpen',
                 'erik': 'cpen'}

https = "https://"

calendar_host = "www.googleapis.com"
calendar_url = "/calendar/v3/calendars/"
google_service_key = "AIzaSyCMt6hpUo-Mrcr82fWafzpkoFAHRBandmA"


@app.route('/', methods=['GET'])
def hello_world():
    return "hello world!"


@app.route('/calendar', methods=['GET'])
def get_calendar():
    """
    Serves a request for calendar data
    :return: http response
    """

    # Read in request parameters.
    calendar_id = request.args.get('calendar_id')
    user = request.args.get('user')
    password = request.args.get('password')
    week = request.args.get('week')

    # Default to current week if not specified.
    if not week:
        week = 0

    # Check for validity of user, password, and calendar id.
    if not calendar_id:
        response = jsonify({'message': 'Calendar ID not specified.'})
        return response, 400

    if user not in trusted_users.keys():
        response = jsonify({'message': 'User invalid.'})
        return response, 401

    if password != trusted_users[user]:
        response = jsonify({'message': 'Incorrect User/Password Conversation'})
        return response, 401

    try:
        week = int(float(week))  # cast to float first rounds off the number.
    except ValueError:
        response = jsonify({'message': 'Incorrect week index input (integer required)'})
        return response, 400

    # Prepare timestamps for start and end of week.
    prev_sunday = get_prev_sunday()
    sys.stderr.write(prev_sunday.isoformat())
    start_sunday = prev_sunday + datetime.timedelta(7 * week)
    end_sunday = start_sunday + datetime.timedelta(7)

    # Prepare request based on the calendar ID given, and make request.
    req = (https +
           calendar_host +
           calendar_url +
           calendar_id +
           '/events?key=' +
           google_service_key +
           '&timeMin=' +
           start_sunday.isoformat() + 'T00:00:00Z' +
           '&timeMax=' +
           end_sunday.isoformat() + 'T00:00:00Z')
    google_resp = requests.get(req)

    # Chain any error we have back to the client (could be dangerous).
    if google_resp.status_code != requests.codes.ok:
        return google_resp.content, google_resp.status_code

    # On success, load json content so that we can parse it.
    google_blob = json.loads(google_resp.content)

    # Prepare our response from the data returned.
    resp = {}
    events = []

    resp['name'] = google_blob['summary']
    resp['description'] = google_blob['description']

    # Iterate over events in the calendar, taking only the info we want.
    for item in google_blob['items']:
        if item['kind'] == 'calendar#event':
            event = {'summary': item['summary'],
                     'start': item['start'],
                     'end': item['end']}
            events.append(event)

    resp['events'] = events
    return jsonify(resp)


def get_prev_sunday():
    """
    Get last Sunday's date object
    :return: Date object corresponding to the Sunday most recently passed
    """
    today = datetime.date.today()
    offset = (today.weekday() + 1) % 7
    return today - datetime.timedelta(offset)

if __name__ == '__main__':
    app.run()
