from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from restaurant import Restaurant


class RestaurantTests(LiveServerTestCase):
    restaurant_endpoint = "/restaurant"

    def create_app(self):
        app = server.app
        app.config['TESTING'] = True

        # Set to 0 to have the OS pick the port.
        app.config['LIVESERVER_PORT'] = 0
        return app

    @classmethod
    def setUpClass(cls):
        pass

    @classmethod
    def tearDownClass(cls):
        pass

    def setUp(self):
        self.restaurant = Restaurant(restaurant_id='test_resto')
        self.params = {'table_id': '0xDEFEC7EDDA7ABA5E'}
        self.request_body = {'name': self.get_server_url()}

    def restaurant_get(self, headers=None):
        return requests.get(self.get_server_url() + self.restaurant_endpoint,
                            params=self.params, headers=headers)

    def restaurant_get_csv(self):
        headers = {'content-type': 'text/csv'}
        return self.restaurant_get(headers=headers)

    def restaurant_post(self):
        return requests.post(self.get_server_url() + self.restaurant_endpoint,
                             json=self.request_body)

    def test_get_success(self):
        r = self.restaurant_get()

        self.assertEqual(r.status_code, server.OK)
        self.assertEqual(r.json().get('restaurant_id', ''), self.restaurant.restaurant_id)

    def test_get_no_id(self):
        self.params = {}
        r = self.restaurant_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_nonexistent(self):
        self.params.update({'table_id': 'garbage'})
        r = self.restaurant_get()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_csv_success(self):
        r = self.restaurant_get_csv()

        self.assertEqual(r.status_code, server.OK)
        self.assertEqual(r.text, self.restaurant.restaurant_id)

    def test_get_csv_nonexistent(self):
        self.params.update({'table_id': 'garbage'})
        r = self.restaurant_get_csv()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertEqual(r.text, 'error')

    def test_post_no_name(self):
        self.request_body = {}
        r = self.restaurant_post()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())