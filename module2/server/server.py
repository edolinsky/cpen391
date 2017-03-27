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

SERVER_ERROR = 500


@app.route('/login', methods=['POST'])
def login_endpoint():
    """
    Handles user login.
    :return: JSON response body and status code.
    """
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

    reg_id = ''
    if 'android_reg_id' in request_body:
        reg_id = request_body['android_reg_id']

    user = User(email=user_email)

    # Validate user/password combination against that in database.
    if user.exists() and user.is_valid(passwd=passwd):

        # If successful, get user affinity and id from database.
        affinity = user.get_affinity()
        user_id = user.get_id()

        user_data = {'user': user_email, 'affinity': affinity,
                     'id': user_id}

        # Update android app reg_id if it was specified.
        if reg_id:
            update_success = user.update_app_id(app_id=reg_id)
            if update_success:
                user_data.update({'android_reg_id': reg_id})

        if not affinity or not user_id:
            user_data.update({'error': 'Unable to fetch user information.'})
            return jsonify(user_data), SERVER_ERROR

        elif affinity in user.staff_users:
            restaurant_id = user.get_my_restaurant(user_id=user_id)

            if not restaurant_id:
                user_data.update({'error': 'Unable to fetch user information.'})
                return jsonify(user_data), SERVER_ERROR
            else:
                user_data.update({'restaurant_id': restaurant_id})
                return jsonify(user_data), OK
        else:
            return jsonify(user_data), OK
    else:
        # If unsuccessful, send error message to user.
        response = jsonify({'user': user_email,
                            'error': 'User not authorized'})
        return response, UNAUTHORIZED


@app.route('/signup', methods=['POST'])
def signup_endpoint():
    """
    Handles user account creation
    :return: JSON response body and status code.
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

    reg_id = ''
    if 'android_reg_id' in request_body:
        reg_id = request_body['android_reg_id']

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
            if 'error' in user_info:
                return jsonify(user_info), SERVER_ERROR
            else:
                # Update android app reg_id if it was specified.
                if reg_id:
                    update_success = user.update_app_id(app_id=reg_id)
                    if update_success:
                        user_info.update({'android_reg_id': reg_id})

                return jsonify(user_info), CREATED

        else:
            response = jsonify({'user': user_email,
                                'restaurant_id': restaurant_id,
                                'error': 'Specified restaurant does not exist.'})
            return response, NOT_FOUND

    # Create customer user.
    else:
        user_info = user.create(password=passwd, affinity=affinity)

        if 'error' in user_info:
            return jsonify(user_info), SERVER_ERROR
        else:
            # Update android app reg_id if it was specified.
            if reg_id:
                update_success = user.update_app_id(app_id=reg_id)
                if update_success:
                    user_info.update({'android_reg_id': reg_id})

            return jsonify(user_info), CREATED


@app.route('/menu', methods=['GET', 'POST'])
def menu_endpoint(item_type='', restaurant_id=''):
    """
    Handles simple requests corresponding to a restaurant's menu.
    :param item_type: Optional item type to filter response, as listed in MenuDB.item_types.
    :param restaurant_id: Unique restaurant ID.
    :return: JSON response body and status code.
    """

    request_body = {}

    # Parse fields from request parameters.
    if request.method == 'GET':
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        item_type = request.args.get('item_type', item_type)

    elif request.method == 'POST':
        request_body = request.get_json()
        restaurant_id = request_body.get('restaurant_id', '')

    # restaurant_id must be specified.
    if not restaurant_id:
        response = jsonify({'error': 'Restaurant ID not specified.'})
        return response, BAD_REQUEST

    restaurant = Restaurant(restaurant_id)

    # Restaurant must exist to create menu.
    if not restaurant.exists():
        response = jsonify({'restaurant_id': restaurant_id,
                            'error': 'Specified restaurant does not exist.'})
        return response, BAD_REQUEST

    if request.method == 'GET':
        menu = Menu(restaurant_id=restaurant_id)

        # Item type is specified; get menu items matching that type.
        if item_type:

            # Specified item type exists.
            if item_type in menu.db.item_types:
                submenu = menu.get_submenu(item_type=item_type)
                if 'error' in submenu.keys():
                    return jsonify(submenu), SERVER_ERROR
                else:
                    return jsonify(submenu), OK

            else:
                # Specified item type does not exist.
                response = jsonify({'restaurant_id': restaurant_id,
                                    'item_type': item_type,
                                    'error': 'Invalid item type.',
                                    'valid_types': menu.db.item_types})
                return response, BAD_REQUEST

        # Item type is not specified; get all menu items.
        else:
            full_menu = menu.get_menu()
            if 'error' in full_menu.keys():
                return jsonify(full_menu), SERVER_ERROR
            else:
                return jsonify(full_menu), OK

    elif request.method == 'POST':

        # List of items must exist and must be non-empty.
        if 'items' not in request_body:
            request_body.update({'error': 'List of items is not specified.'})
            return jsonify(request_body), BAD_REQUEST

        elif len(request_body['items']) < 1:
            request_body.update({'error': 'List of items is empty.'})
            return jsonify(request_body), BAD_REQUEST

        menu = Menu(restaurant_id=restaurant_id)
        menu_response = menu.create_menu(menu_info=request_body)

        if 'error' in menu_response:
            return jsonify(menu_response), SERVER_ERROR
        else:
            return jsonify(menu_response), OK


@app.route('/order', methods=['GET', 'POST', 'PUT'])
def order_endpoint(order_id='', restaurant_id='', customer_id='', table_id=''):
    """
    Handles user- or staff-initiated requests pertaining to individual orders.
    This endpoint does not handle order status.
    :param order_id: Unique order ID (only for GET/PUT).
    :param restaurant_id: Unique restaurant ID.
    :param customer_id: Unique user ID of customer.
    :param table_id: Unique hub device ID.
    :return: JSON response body and status code.
    """

    if request.method in ['POST', 'PUT']:
        # Request has json body.
        order_request = flask.request.get_json()
        restaurant_id = order_request.get('restaurant_id', '')
        customer_id = order_request.get('customer_id', '')
        table_id = order_request.get('table_id', '')

    else:
        # GET request
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        order_id = request.args.get('order_id', order_id)
        customer_id = request.args.get('customer_id', customer_id)
        table_id = request.args.get('table_id', table_id)
        order_request = {}

        # Order ID must be non-empty.
        if not order_id:
            order_request.update({'error': 'Order ID must be specified.'})
            return jsonify(order_request), BAD_REQUEST

    if not restaurant_id:
        order_request.update({'error': 'Restaurant ID is not specified.'})
        return jsonify(order_request), BAD_REQUEST

    if not customer_id:
        order_request.update({'error': 'Customer ID is not specified.'})
        return jsonify(order_request), BAD_REQUEST

    if not table_id:
        order_request.update({'error': 'Table ID is not specified.'})
        return jsonify(order_request), BAD_REQUEST

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
        if request.content_type == 'text/csv':
            return order_info
        elif 'error' in order_info:
            return jsonify(order_info), SERVER_ERROR
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
            return jsonify(order_response), SERVER_ERROR
        else:
            return jsonify(order_response), OK

    if request.method == 'PUT':
        # PUT -> update order (insert/update)
        pass


@app.route('/orders', methods=['GET', 'PATCH'])
def orders_endpoint(query='open', restaurant_id=''):
    """
    Handles waitstaff-side requests for restaurant orders, and updates
    to order item status.
    :param query: one of the query types as listed in supported_queries.
    :param restaurant_id: unique restaurant ID.
    :return: JSON or CSV response body and status code.
    """
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
        if not restaurant.exists():
            response = jsonify({'error': 'Specified restaurant does not exist.'})
            return response, BAD_REQUEST

        orders = {}
        # Switch on query types, executing corresponding query.
        if query == 'open':
            orders = restaurant.get_open_orders()

        if 'error' in orders:
            return jsonify(orders), SERVER_ERROR
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

        # Restaurant must exist in database.
        restaurant = Restaurant(restaurant_id=restaurant_id)
        if not restaurant.exists():
            update_request.update({'error': 'Specified restaurant does not exist.'})
            return jsonify(update_request), BAD_REQUEST

        order = Order(restaurant_id=restaurant_id)
        update_info = order.update_status(update_info=update_request)

        if 'error' in update_info:
            return jsonify(update_info), SERVER_ERROR
        else:
            return jsonify(update_info), OK


@app.route('/restaurant', methods=['GET', 'POST'])
def restaurant_endpoint(table_id=''):
    """
    Handles creation and queries for restaurant data.
    :param table_id: hub device ID.
    :return: JSON or CSV response body and status code.
    """

    if request.method == 'GET':
        hub_id = request.args.get('table_id', table_id)

        hub = Hub('')
        hub.get_restaurant_id(hub_id=hub_id)

        if request.content_type == 'text/csv':
            print hub.restaurant_id
            if hub.restaurant_id:
                return hub.restaurant_id, OK
            else:
                return 'error', BAD_REQUEST
        else:
            if hub.restaurant_id:
                return jsonify({'table_id': hub_id,
                                'restaurant_id': hub.restaurant_id}), OK
            else:
                return jsonify({'table_id': hub_id,
                                'error': 'Specified table ID is not affiliated with a restaurant.'}), BAD_REQUEST

    elif request.method == 'POST':
        request_body = flask.request.get_json()
        # Restaurant name must be supplied in request body.
        if 'name' in request_body:
            restaurant_name = request_body['name']
        else:
            request_body.update({'error': 'Restaurant name not specified.'})
            return jsonify(request_body), BAD_REQUEST

        # Create new restaurant, and return result to user.
        restaurant = Restaurant(restaurant_id='')
        restaurant_info = restaurant.create(name=restaurant_name)

        if 'error' in restaurant_info:
            return jsonify(restaurant_info), SERVER_ERROR
        else:
            return jsonify(restaurant_info), OK


@app.route('/call_server', methods=['POST'])
def call_server_endpoint():
    """
    Gateway for mobile notifications as initiated by a table hub.
    :return: JSON response body and status code.
    """
    request_body = flask.request.get_json()

    # Restaurant ID and Table ID must be specified in request body.
    if 'restaurant_id' in request_body:
        restaurant_id = request_body['restaurant_id']
    else:
        request_body.update({'error': 'Restaurant ID is not specified.'})
        return jsonify(request_body), BAD_REQUEST
    if 'table_id' in request_body:
        table_id = request_body['table_id']
    else:
        request_body.update({'error': 'Table ID is not specified.'})
        return jsonify(request_body), BAD_REQUEST

    hub = Hub(restaurant_id=restaurant_id)
    user = User('')

    # Get current attendant user from hub information.
    attendant_id = hub.get_attendant_id(hub_id=table_id)
    attendant_app_id = user.get_app_id(attendant_id)

    # Get table name for message body.
    table_name = hub.get_table_name(hub_id=table_id)

    # Trigger notification to attendant.
    success = hub.trigger_notification(attendant_app_id=attendant_app_id,
                                       table_name=table_name)
    if success:
        request_body.update({'message': 'Notification Successful.'})
        return jsonify(request_body), OK
    else:
        request_body.update({'error': 'Could not send Notification.'})
        return jsonify(request_body), SERVER_ERROR


@app.route('/server_hub_map', methods=['GET', 'POST', 'DELETE'])
def server_hub_map_endpoint(restaurant_id='', attendant_id=''):
    """
    Handles updates to waitstaff/table mappings, which is used for
    mobile notifications.
    :return: JSON response body and status code.
    """
    if request.method == 'POST':
        # POST Request.
        request_body = flask.request.get_json()
        restaurant_id = request_body.get('restaurant_id', '')
    elif request.method == 'GET':
        # GET Request.
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        request_body = {}
    else:
        # DELETE Request.
        restaurant_id = request.args.get('restaurant_id', restaurant_id)
        attendant_id = request.args.get('attendant_id', attendant_id)
        request_body = {}

    if not restaurant_id:
        request_body.update({'error': 'Restaurant ID not specified.'})
        return jsonify(request_body), BAD_REQUEST

    restaurant = Restaurant(restaurant_id=restaurant_id)
    if not restaurant.exists():
        request_body.update({'error': 'Specified restaurant does not exist.'})
        return jsonify(request_body), BAD_REQUEST

    if request.method == 'POST':
        # update keys.
        if 'mappings' not in request_body:
            request_body.update({'error': 'Mappings not specified.'})
            return jsonify(request_body), BAD_REQUEST
        elif len(request_body['mappings']) < 1:
            request_body.update({'error': 'Mappings list is empty.'})
            return jsonify(request_body), BAD_REQUEST

        mappings = request_body['mappings']

        for mapping in mappings:
            user = User('')
            user_id = mapping.get('attendant_id', '')
            user.get_email(user_id=user_id)

            hub = Hub(restaurant_id=restaurant_id)
            hub_id = mapping.get('table_id', '')

            if not user.exists():
                request_body.update({'error': 'Specified user {} does not exist.'.format(user_id)})
                return jsonify(request_body), BAD_REQUEST

            elif user.get_my_restaurant(user_id=user_id) != restaurant_id:
                request_body.update(
                    {'error': 'Specified user {} is not affiliated with specified restaurant.'.format(user_id)})
                return jsonify(request_body), UNAUTHORIZED

            if not hub.is_registered(hub_id=hub_id):
                request_body.update({
                    'error': 'Specified table ID {} is not affiliated with specified restaurant.'.format(hub_id)})
                return jsonify(request_body), UNAUTHORIZED

        mapping_info = restaurant.update_staff_hub_mappings(mapping_info=request_body)
        return jsonify(mapping_info), CREATED

    elif request.method == 'GET':
        # GET all mappings in restaurant (with emails).
        mappings = restaurant.get_staff_hub_mappings()
        request_body.update(mappings)

        if 'error' in mappings:
            return jsonify(request_body), SERVER_ERROR
        else:
            return jsonify(request_body), OK

    elif request.method == 'DELETE':

        if not attendant_id:
            request_body.update({'error': 'User ID not specified.'})
            return jsonify(request_body), BAD_REQUEST

        user = User('')
        user.get_email(user_id=attendant_id)

        if not user.exists():
            request_body.update({'error': 'Specified user does not exist.'})
            return jsonify(request_body), BAD_REQUEST

        elif user.get_my_restaurant(user_id=attendant_id) != restaurant_id:
            request_body.update({'error': 'User restaurant combination does not match.'})
            return jsonify(request_body), UNAUTHORIZED

        response = restaurant.remove_staff_from_mappings(staff_id=attendant_id)
        request_body.update(response)

        if 'error' in response:
            return jsonify(response), SERVER_ERROR
        else:
            return jsonify(response), OK


@app.route('/update_fcm_id', methods=['POST'])
def update_fcm_id_endpoint():
    """
    Handes updates to firebase cloud messaging identifiers.
    :return: JSON response body and status code.
    """
    request_body = flask.request.get_json()

    # Check for required fields.
    if 'user' in request_body:
        user_email = request_body['user']
    else:
        response = jsonify({'error': 'Email not specified.'})
        return response, BAD_REQUEST

    if 'android_reg_id' in request_body:
        android_reg_id = request_body['android_reg_id']
    else:
        request_body.update({'error': 'FCM ID not specified.'})
        return jsonify(request_body), BAD_REQUEST

    user = User(email=user_email)

    # Make sure specified user exists in database.
    if not user.exists():
        request_body.update({'error': 'User does not exist.'})
        return jsonify(request_body), UNAUTHORIZED
    else:
        # Update application ID.
        update_success = user.update_app_id(app_id=android_reg_id)
        if update_success:
            request_body.update({'message': 'FCM ID Update successful.'})
            return jsonify(request_body), CREATED
        else:
            request_body.update({'error': 'Could not update FCM ID.'})
            return jsonify(request_body), SERVER_ERROR


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
