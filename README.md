# con-planner-75
Event planning app suite being developed for local SciFi convention.


Android:
Port Forwarding to your phone

This is needed to demo the connection to the website without us having create an actual webserver.
I am not sure about using firefox, but this works for Chrome.

Plug in your Android phone with developer mode enabled.
In chrome, go to chrome://inspect/#devices
Click Port Forwarding.
For Port, enter 3000 and for IP/Port, enter 127.0.0.1:3000
With your version of the website running, check that your phone can access 127.0.0.1:3000 from your browser

Methods to work with objects in the Android environment:

List<Convention> conventionList = new SearchConventionsTask().execute("o").get();

This gets a list of all conventions that match the key word (the "o" in this case).
Returns a null on an error.

Convention c = new DownloadConventionTask(this).execute("holds").get();

This will get a convention with the exact matching name ("holds" in the above line) and place it in the device memory as a file.
It will then parse the Convention into an object and return it.
It returns null on an error.
Make sure that you use the name from the convention search as the download query.

Convention c = AppUtils.parseConvention("holds");

Gets a downloaded convention that matches the supplied name.