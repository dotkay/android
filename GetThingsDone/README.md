# Pre-work - GetThingsDone

**GetThingsDone** is an android app that allows building a todo list and basic todo items management functionality including
- adding new items
- editing existing items
- deleting existing items

Submitted by: Kalyan

Time spent: around 3-4 hours

The following **required** functionality is completed:
* [X] User can **successfully add and remove items** from the todo list
* [X] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [X] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [ ] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [ ] List anything else that you can get done to improve the app functionality!

## Video Walkthrough 


## Notes
Version 0.0
- Most of the time was spent in understanding small Android details like EditText alignment, etc.
- Getting used to making Android Studio add the onClick handler skeleton code took a while
- Understanding Adapters, AdapterView was interesting and spent some time reading about it
- Still see a bunch of some wifi error messages thrown by Android Studio, but the app itself seems to run fine

Version 1.0
- Most of the time was spent on trying to write and read from files. It still doesn't work fine. There's a dangling item
in the list when when all the items were deleted. Have to spend more time/effort to debug it and clean it up
- Getting around capturing RadioButton selected item from Java took a significantly longer time than expected

Version 2.0
- Added SQLite database for persistence. 
- Still having problems losing updates when the app is restarted. Although added items are persistent. Have to debug more.

Version 2.1
- Bug fix in database update() function
- Added ability to debug the android emulator app transactions with DB using Stetho

## License

    Copyright [2017] [Kalyan]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
