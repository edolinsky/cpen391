import threading
from test.test_login import LoginTests
from test.test_signup import SignupTests

suites = ['login', 'signup']


def worker(suite_name):

    if suite_name == 'login':
        test = LoginTests()
    elif suite_name == 'signup':
        test = SignupTests
    else:
        return

    test.run()

if __name__ == '__main__':

    threaded = False    # Do not change unless wsgi/gunicorn is configured
    if threaded:
        threads = []
        for suite in suites:
            t = threading.Thread(target=worker, args=suite,)
            threads.append(t)
            t.start()
    else:
        for suite in suites:
            worker(suite)
