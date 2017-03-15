import flask
import os

from flask import request, jsonify
from user import User
from menu import Menu
from restaurant import Restaurant
from order import Order

app = flask.Flask(__name__)

OK = 200
CREATED = 201

BAD_REQUEST = 400
UNAUTHORIZED = 401
FORBIDDEN = 403
NOT_FOUND = 404
TEAPOT = 418

SERVER_ERRROR = 500


@app.route('/login', methods=['POST'])
def login_endpoint():
    request_body = request.get_json()
    if 'user' in request_body:
        user_email = request_body['user']
    else:
        response = jsonify({'error': 'Email not specified.'})
        return response, BAD_REQUEST

    if 'password' in request_body:
        passwd = request_body['password']
    else:
        response = jsonify({'error': 'Password not specified.'})
        return response, BAD_REQUEST

    user = User(email=user_email)

    # Validate user/password combination against that in database.
    if user.exists() and user.is_valid(passwd=passwd):

        # If successful, get user affinity and id from database.
        affinity = user.get_affinity()
        user_id = user.get_id()

        if not affinity or not user_id:
            response = jsonify({'error': 'Unable to fetch user information.',
                                'user': user_email})
            return response, SERVER_ERRROR
        elif affinity == 'staff':
            restaurant_id = user.get_my_restaurant(user_id=user_id)
            if not restaurant_id:
                response = jsonify({'error': 'Unable to fetch user information.',
                                    'user': user_email})
                return response, SERVER_ERRROR
            else:
                response = jsonify({'user': user_email, 'affinity': affinity,
                                    'id': user_id, 'restaurant_id': restaurant_id})
                return response, OK
        else:
            response = jsonify({'user': user_email, 'affinity': affinity, 'id': user_id})
            return response, OK
    else:
        # If unsuccessful, send error message to user.
        response = jsonify({'user': user_email,
                            'error': 'User not authorized'})
        return response, UNAUTHORIZED


@app.route('/signup', methods=['POST'])
def signup_endpoint():

    # Parse request body and throw errors if required fields are not included.
    request_body = request.get_json()
    if 'affinity' in request_body:
        affinity = request_body['affinity']
    else:
        affinity = 'customer'

    if 'user' in request_body:
        user_email = request_body['user']
    else:
        response = jsonify({'error': 'Email not specified.'})
        return response, BAD_REQUEST

    if 'password' in request_body:
        passwd = request_body['password']
    else:
        response = jsonify({'error': 'Password not specified.'})
        return response, BAD_REQUEST

    user = User(email=user_email)

    # Do not allow a user to be created if email already exists
    if user.exists():
        response = jsonify({'user': user_email,
                            'error': 'User already exists.'})
        return response, FORBIDDEN

    # Create staff user.
    elif affinity == 'staff':
        try:
            restaurant_id = request_body['restaurant_id']
        except KeyError:
            response = jsonify({'user': user_email,
                                'affinity': affinity,
                                'error': 'Restaurant ID is required to sign up as {}.'.format(affinity)})
            return response, 400

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
        user_info = user.create(password=passwd, affinity=affinity)

        if 'error' in user_info.keys():
            return jsonify(user_info), SERVER_ERRROR
        else:
            return jsonify(user_info), CREATED


@app.route('/menu', methods=['GET'])
def menu_endpoint(item_type='', restaurant_id=''):
    restaurant_id = request.args.get('restaurant_id', restaurant_id)
    item_type = request.args.get('item_type', item_type)

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
        if item_type in menu.db.item_types:
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
                                'valid_types': restaurant.db.item_types})
            return response, BAD_REQUEST

    # Item type is not specified; get all menu items.
    else:
        full_menu = menu.get_menu()
        if 'error' in full_menu.keys():
            return jsonify(full_menu), SERVER_ERRROR
        else:
            return jsonify(full_menu), OK


@app.route('/order', methods=['GET', 'POST', 'PUT', 'DELETE'])
def order_endpoint(order_id='', restaurant_id='', customer_id='', table_id=''):

    if request.method in ['POST', 'PUT', 'DELETE']:
        order_request = flask.request.get_json()

        if 'restaurant_id' in order_request:
            restaurant_id = order_request['restaurant_id']
        else:
            order_request.update({'error': 'Restaurant ID is not specified.'})
            return jsonify(order_request), BAD_REQUEST

        if 'customer_id' in order_request:
            customer_id = order_request['customer_id']
        else:
            order_request.update({'error': 'Customer ID is not specified.'})
            return jsonify(order_request), BAD_REQUEST

        if 'table_id' in order_request:
            table_id = order_request['table_id']
        else:
            order_request.update({'error': 'Table ID is not specified.'})
            return jsonify(order_request), BAD_REQUEST
    else:
        # GET request
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        order_id = request.args.get('order_id', order_id)
        customer_id = request.args.get('customer_id', customer_id)
        table_id = request.args.get('table_id', table_id)
        order_request = {}

    # Restaurant must exist.
    restaurant = Restaurant(restaurant_id=restaurant_id)
    if not restaurant.exists():
        order_request.update({'error': 'Specified restaurant does not exist.'})
        return order_request, BAD_REQUEST

    # Customer must exist.
    user = User('')
    user.get_email(customer_id)
    if not user.exists():
        order_request.update({'error': 'Specified customer does not exist.'})
        return order_request, BAD_REQUEST

    # Table must exist and be affiliated with the restaurant.
    # todo: implement device class & corresponding DB

    order = Order(customer_id=customer_id, restaurant_id=restaurant_id, table_id=table_id)

    if request.method == 'GET':
        order_info = order.get_order(order_id=order_id,
                                     restaurant_id=restaurant_id,
                                     content_type=request.content_type)
        if request.content_type == 'text/csv':
            # Check for error message in string
            # Send back to HUB device
            pass
        elif 'error' in order_info:
            return jsonify(order_info), SERVER_ERRROR
        else:
            return jsonify(order_info), OK

    if request.method == 'POST':
        if 'items' not in order_request:
            order_request.update({'error': 'List of items is not specified.'})
            return jsonify(order_request), BAD_REQUEST

        elif len(order_request['items']) < 1:
            order_request.update({'error': 'List of items is empty.'})
            return jsonify(order_request), BAD_REQUEST

        order_response = order.place_order(order_request)
        if 'error' in order_response:
            return jsonify(order_response), SERVER_ERRROR
        else:
            return jsonify(order_response), OK

    if request.method == 'PUT':
        # PUT -> update order (insert/update)
        pass

    if request.method == 'DELETE':
        # DELETE -> cancel order (delete)
        # Should only be done by staff
        pass


@app.route('/hello')
def hello_world():
    return jsonify({'message': 'Hello World!'}), OK


@app.route('/teapot')
def teapot():
    return jsonify({'type': 'teapot'}), TEAPOT


if __name__ == '__main__':
    app.secret_key = os.environ['secret_key']

    if os.environ['runtime'] == 'production':
        pass
    else:
        app.run()
