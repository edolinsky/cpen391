import flask
import os

from flask import request, jsonify
from user import User
from menu import Menu
from restaurant import Restaurant

app = flask.Flask(__name__)

OK = 200
CREATED = 201

BAD_REQUEST = 400
UNAUTHORIZED = 401
FORBIDDEN = 403
NOT_FOUND = 404
TEAPOT = 418

SERVER_ERRROR = 500


@app.route('/login', methods=['GET', 'POST'])
def login_endpoint(user_email='', passwd='', affinity='', restaurant_id=''):
    user_email = request.args.get('user', user_email)
    passwd = request.args.get('password', passwd)
    affinity = request.args.get('affinity', affinity)
    restaurant_id = request.args.get('restaurant_id', restaurant_id)

    user = User(email=user_email)

    # Ensure that email and password are specified.
    if not user_email:
        response = jsonify({'error': 'Email not specified.'})
        return response, BAD_REQUEST

    if not passwd:
        response = jsonify({'error': 'Password not specified.'})
        return response, BAD_REQUEST

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
            response = jsonify({'user': user_email,
                                'error': 'User already exists.'})
            return response, FORBIDDEN

        # Create staff user.
        elif affinity == 'staff':
            restaurant = Restaurant(restaurant_id)

            # Restaurant exists: create user under restaurant
            if restaurant_id and restaurant.exists():
                user_info = user.create(password=passwd, restaurant_id=restaurant_id, affinity=affinity)
                if 'error' in user_info.keys():
                    return jsonify(user_info), SERVER_ERRROR
                else:
                    return jsonify(user_info), CREATED

            else:
                response = jsonify({'user': user_email,
                                    'restaurant_id': restaurant_id,
                                    'error': 'Specified restaurant does not exist.'})
                return response, NOT_FOUND

        # Create customer user.
        else:
            # Create customer account
            if not affinity:
                affinity = 'customer'
            user_info = user.create(password=passwd, affinity=affinity)

            if 'error' in user_info.keys():
                return jsonify(user_info), SERVER_ERRROR
            else:
                return jsonify(user_info), CREATED


@app.route('/menu', methods=['GET'])
def menu_endpoint(item_type='', restaurant_id=''):
    restaurant_id = request.args.get('restaurant_id', restaurant_id)
    item_type = request.args.get('item_type', item_type)
    item_types = ['drink', 'alcoholic', 'appetizer', 'entree', 'dessert', 'merchandise']

    if not restaurant_id:
        response = jsonify({'error': 'Restaurant ID not specified.'})
        return response, BAD_REQUEST

    restaurant = Restaurant(restaurant_id)
    menu = Menu(restaurant_id=restaurant_id)

    # Restaurant must exist to retrieve menu.
    if not restaurant.exists():
        response = jsonify({'restaurant_id': restaurant_id,
                            'error': 'Specified restaurant does not exist.'})
        return response, BAD_REQUEST

    # Item type is specified; get menu items matching that type.
    elif item_type:

        # Specified item type exists.
        if item_type in item_types:
            submenu = menu.get_submenu(item_type=item_type)
            if 'error' in submenu.keys():
                return jsonify(submenu), SERVER_ERRROR
            else:
                return jsonify(submenu), OK

        # Specified item type does not exist.
        else:
            response = jsonify({'restaurant_id': restaurant_id,
                                'item_type': item_type,
                                'error': 'Invalid item type.',
                                'valid_types': item_types})
            return response, BAD_REQUEST

    # Item type is not specified; get all menu items.
    else:
        full_menu = menu.get_menu()
        if 'error' in full_menu.keys():
            return jsonify(full_menu), SERVER_ERRROR
        else:
            return jsonify(full_menu), OK


@app.route('/order', methods=['GET', 'POST', 'PUT', 'DELETE'])
def order_endpoint():
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


@app.route('/teapot')
def teapot():
    return jsonify({'type': 'teapot'}), TEAPOT


if __name__ == '__main__':
    app.secret_key = os.environ['secret_key']
    app.run()
