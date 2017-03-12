import flask
import os

from flask import request, jsonify
from user import User
from restaurant import Restaurant

app = flask.Flask(__name__)

OK = 200
CREATED = 201

UNAUTHORIZED = 401
FORBIDDEN = 403
NOT_FOUND = 404


@app.route('/login', methods=['GET', 'POST'])
def login():
    user_email = request.args['user']
    passwd = request.args['password']
    affinity = request.args['affinity']
    restaurant_id = request.args['restaurant_id']

    user = User(email=user_email)

    # Ensure that email and password are specified.
    if not user_email:
        response = jsonify({'error': 'Email required.'})
        return response, UNAUTHORIZED

    if not passwd:
        response = jsonify({'error': 'Password required.'})
        return response, UNAUTHORIZED

    # GET: User login
    if request.method == 'GET':
        # Validate user/password combination against that in database.
        if user.is_valid(passwd=passwd):

            # If successful, get user affinity from database.
            affinity = user.get_affinity()
            response = jsonify({'user': user_email, 'affinity': affinity})
            return response, OK
        else:
            # If unsuccessful, send error message to user.
            response = jsonify({'user': user_email,
                                'error': 'User not authorized'})
            return response, UNAUTHORIZED

    # POST: User sign up
    if request.method == 'POST':

        # Do not allow a user to be created if email already exists
        if user.exists():
            response = jsonify({'user':user_email,
                                'error': 'User already exists.'})
            return jsonify(response), FORBIDDEN

        else:
            if affinity == 'staff':
                restaurant = Restaurant(restaurant_id)
                if restaurant_id and restaurant.exists():
                    user.create(password=passwd, restaurant_id=restaurant_id, affinity=affinity)
                else:
                    response = jsonify({'user': user_email,
                                        'restaurant_id': restaurant_id,
                                        'error': 'Specified Restaurant does not exist.'})
                    return response, NOT_FOUND
            else:
                # Create customer account
                if not affinity:
                    affinity = 'customer'


@app.route('/menu', methods=['GET'])
def menu():
    pass


@app.route('/order', methods=['GET', 'POST', 'PUT', 'DELETE'])
def order():
    # todo: implement

    if request.method == 'GET':
        # GET -> get order (select)
        pass

    if request.method == 'POST':
        # POST -> place order (insert)
        pass

    if request.method == 'PUT':
        # PUT -> update order (insert/update)
        pass

    if request.method == 'DELETE':
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
