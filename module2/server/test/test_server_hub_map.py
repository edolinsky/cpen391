from __future__ import absolute_import
from flask_testing import LiveServerTestCase
import requests
import server
from hub import Hub


class ServerHubMapTests(LiveServerTestCase):
    server_hub_map_endpoint = "/server_hub_map"

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
        self.restaurant_id = 'test_resto'
        self.attendant_id = '71a182e218'
        self.hub_id = '0xDEFEC7EDDA7ABA5E'
        self.get_params = {'restaurant_id': self.restaurant_id}
        self.delete_params = {'restaurant_id': self.restaurant_id,
                              'attendant_id': self.attendant_id}
        self.post_body = {'restaurant_id': self.restaurant_id,
                          'mappings': [
                              {
                                  'attendant_id': self.attendant_id,
                                  'table_id': self.hub_id
                              }
                          ]
                          }

    def get_server_hub_map(self):
        return requests.get(self.get_server_url() + self.server_hub_map_endpoint,
                            params=self.get_params)

    def post_server_hub_map(self):
        return requests.post(self.get_server_url() + self.server_hub_map_endpoint,
                             json=self.post_body)

    def delete_server_hub_map(self):
        return requests.delete(self.get_server_url() + self.server_hub_map_endpoint,
                               params=self.delete_params)

    def test_get_success(self):
        r = self.get_server_hub_map()

        self.assertEqual(r.status_code, server.OK)
        self.assertTrue('mappings' in r.json())

    def test_get_no_restaurant(self):
        self.get_params.pop('restaurant_id')
        r = self.get_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_get_invalid_restaurant(self):
        self.get_params.update({'restaurant_id': 'garbage'})
        r = self.get_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_success(self):
        r1 = self.delete_server_hub_map()

        self.assertEqual(r1.status_code, server.OK)

        r2 = self.post_server_hub_map()

        hub = Hub(restaurant_id=self.restaurant_id)

        self.assertEqual(r2.status_code, server.CREATED)
        self.assertTrue(hub.get_attendant_id(self.hub_id), self.attendant_id)

    def test_post_no_restaurant(self):
        self.post_body.pop('restaurant_id')

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_invalid_restaurant(self):
        self.post_body.update({'restaurant_id': 'garbage'})

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_user(self):
        self.post_body['mappings'][0].pop('attendant_id')

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_invalid_user(self):
        self.post_body['mappings'][0].update({'attendant_id': 'garbage'})

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_no_table(self):
        self.post_body['mappings'][0].pop('table_id')

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_post_invalid_table(self):
        self.post_body['mappings'][0].update({'table_id': 'garbage'})

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.UNAUTHORIZED)
        self.assertTrue('error' in r.json())

    def test_post_no_mappings(self):
        self.post_body.pop('mappings')

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_post_empty_mappings(self):
        self.post_body.update({'mappings': []})

        r = self.post_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_delete_no_user(self):
        self.delete_params.pop('attendant_id')

        r = self.delete_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_delete_invalid_user(self):
        self.delete_params.update({'attendant_id': 'garbage'})

        r = self.delete_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_delete_no_restaurant(self):
        self.delete_params.pop('restaurant_id')

        r = self.delete_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_delete_invalid_restaurant(self):
        self.delete_params.update({'restaurant_id': 'garbage'})

        r = self.delete_server_hub_map()

        self.assertEqual(r.status_code, server.BAD_REQUEST)
        self.assertTrue('error' in r.json())

    def test_delete_success(self):
        r1 = self.delete_server_hub_map()

        self.assertEqual(r1.status_code, server.OK)
        self.assertTrue('message' in r1.json())

        r2 = self.delete_server_hub_map()

        self.assertEqual(r2.status_code, server.OK)
        self.assertTrue('message' in r2.json())

        # Place mapping back in db.
        r3 = self.post_server_hub_map()

        hub = Hub(restaurant_id=self.restaurant_id)

        self.assertEqual(r3.status_code, server.CREATED)
        self.assertTrue(hub.get_attendant_id(self.hub_id), self.attendant_id)
