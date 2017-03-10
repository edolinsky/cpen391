import flask
import os

from flask import request, jsonify
from user import User

app = flask.Flask(__name__)

OK = 200
UNAUTHORIZED = 401
NOT_FOUND = 404


@app.route('/login', methods=['GET'])
def login():
    user_name = request.args['user']
    passwd = request.args['password']

    user = User(email=user_name)

    # Validate user/password combination against that in database.
    if user.is_valid(passwd=passwd):

        # If successful, get user affinity from database.
        affinity = user.get_affinity()
        response = jsonify({'user': user_name, 'affinity': affinity})
        return response, OK
    else:

        # If unsuccessful, send error message to user.
        response = jsonify({'user': user_name,
                            'error': 'User not authorized'})
        return response, UNAUTHORIZED


@app.route('/order', methods=['GET', 'POST', 'PUT', 'DELETE'])
def order():
    # todo: implement

    # GET -> get order (select)
    # POST -> place order (insert)
    # PUT -> update order (insert/update)
    # DELETE -> cancel order (delete)
    pass


@app.route('/hello')
def hello_world():
    return jsonify({'message': 'Hello World!'}), OK


if __name__ == '__main__':
    app.secret_key = os.environ['secret_key']
    db_user = os.environ['db_user']
    db_passwd = os.environ['db_passwd']
    db_host = os.environ['db_host']
    db_port = os.environ['db_port']
    db_database = os.environ['db']

    app.run()
