import flask
import os

from flask import request, jsonify
from user import User
from menu import Menu
from hub import Hub
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

        elif affinity in user.staff_users:
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
    """
    Handles user account creation
    :return:
    """

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
    elif affinity in user.staff_users:
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
    """
    Handles simple requests corresponding to a restaurant's menu.
    :param item_type:
    :param restaurant_id:
    :return:
    """

    # Parse fields from request parameters.
    restaurant_id = request.args.get('restaurant_id', restaurant_id)
    item_type = request.args.get('item_type', item_type)

    # restaurant_id must be specified.
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


@app.route('/order', methods=['GET', 'POST', 'PUT'])
def order_endpoint(order_id='', restaurant_id='', customer_id='', table_id=''):
    """
    Handles user- or staff-initiated requests pertaining to individual orders.
    This endpoint does not handle order status.
    :param order_id:
    :param restaurant_id:
    :param customer_id:
    :param table_id:
    :return:
    """

    if request.method in ['POST', 'PUT']:
        # Request has json body.
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

    # Restaurant must exist in database.
    restaurant = Restaurant(restaurant_id=restaurant_id)
    if not restaurant.exists():
        order_request.update({'error': 'Specified restaurant does not exist.'})
        return jsonify(order_request), BAD_REQUEST

    # Customer must exist in database.
    user = User('')
    user.get_email(customer_id)
    if not user.exists():
        order_request.update({'error': 'Specified customer does not exist.'})
        return jsonify(order_request), BAD_REQUEST

    # Table must exist and be affiliated with the restaurant.
    hub = Hub(restaurant_id=restaurant_id)
    if not hub.is_registered(hub_id=table_id):
        order_request.update({'error': 'Specified table is not registered to this restaurant.'})
        return jsonify(order_request), UNAUTHORIZED

    order = Order(restaurant_id=restaurant_id)

    if request.method == 'GET':
        order_info = order.get_order(order_id=order_id,
                                     restaurant_id=restaurant_id,
                                     content_type=request.content_type)
        if request.content_type == 'application/csv':
            return order_info
        elif 'error' in order_info:
            return jsonify(order_info), SERVER_ERRROR
        else:
            return jsonify(order_info), OK

    if request.method == 'POST':

        # List of items must exist and must be non-empty.
        if 'items' not in order_request:
            order_request.update({'error': 'List of items is not specified.'})
            return jsonify(order_request), BAD_REQUEST

        elif len(order_request['items']) < 1:
            order_request.update({'error': 'List of items is empty.'})
            return jsonify(order_request), BAD_REQUEST

        order_response = order.place_order(order=order_request,
                                           customer_id=customer_id,
                                           table_id=table_id)
        if 'error' in order_response:
            return jsonify(order_response), SERVER_ERRROR
        else:
            return jsonify(order_response), OK

    if request.method == 'PUT':
        # PUT -> update order (insert/update)
        pass


@app.route('/orders', methods=['GET', 'PATCH'])
def orders_endpoint(query='open', restaurant_id=''):
    supported_queries = ['open']

    if request.method == 'GET':
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        query = request.args.get('query', query)

        if not restaurant_id:
            response = jsonify({'error': 'Restaurant ID is not specified.'})
            return response, BAD_REQUEST
        if query not in supported_queries:
            response = jsonify({'error': 'Specified query is not supported.'})
            return response, BAD_REQUEST

        restaurant = Restaurant(restaurant_id=restaurant_id)

        orders = {}
        # Switch on query types, executing corresponding query.
        if query == 'open':
            orders = restaurant.get_open_orders()

        if 'error' in orders:
            return jsonify(orders), SERVER_ERRROR
        else:
            return jsonify(orders), OK

    if request.method == 'PATCH':
        update_request = flask.request.get_json()

        # Restaurant must be specified in request body.
        if 'restaurant_id' in update_request:
            restaurant_id = update_request['restaurant_id']
        else:
            update_request.update({'error': 'Restaurant ID is not specified.'})
            return jsonify(update_request), BAD_REQUEST

        # Restaurant must exist.
        restaurant = Restaurant(restaurant_id=restaurant_id)
        if not restaurant.exists():
            update_request.update({'error': 'Specified restaurant does not exist.'})
            return update_request, BAD_REQUEST

        order = Order(restaurant_id=restaurant_id)
        update_info = order.update_status(update_info=update_request)

        if 'error' in update_info:
            return jsonify(update_info), SERVER_ERRROR
        else:
            return jsonify(update_info), OK


@app.route('/hello')
def hello_world():
    return jsonify({'message': 'Hello World!'}), OK


@app.route('/teapot')
def teapot():
    return jsonify({'type': 'teapot'}), TEAPOT


if __name__ == '__main__':
    app.secret_key = os.environ['secret_key']

    if os.environ['runtime'] == 'production':
        app.run('0.0.0.0', debug=False, port=80)
    else:
        app.run()
