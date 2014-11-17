CircularViewPager
=================

This is my implementation of a circular `ViewPager`. If you build the sample you will see an example.

You can also download the sample app in the Google Play Store: [demo](https://play.google.com/store/apps/details?id=com.tobishiba.circularviewpager)

<img src="https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.tobishiba.circularviewpager&chs=180x180&choe=UTF-8&chld=L|2' alt=" />

[![Alt text for your video](http://img.youtube.com/vi/8L0gHiKz8fI/0.jpg)](https://www.youtube.com/watch?v=8L0gHiKz8fI&feature=youtu.be)

##Features
 - enables endless scrolling
 - very easy to use
 - no limitations, you can use it like a normal `ViewPager`

##Usage

Add a usual ViewPager to your layout and set the `overScrollMode` to `never` to avoid bad user experience:

```xml
	<android.support.v4.view.ViewPager
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:overScrollMode="never"/> 
```
</br>
</br>

Create an adapter for your `ViewPager` and let it extend the `BaseCircularViewPagerAdapter` and implement it's methods:

```java 
public class MemeCircularViewPagerAdapter extends BaseCircularViewPagerAdapter<Meme> {
private final Context mContext;

    public MemeCircularViewPagerAdapter(final Context context, final FragmentManager fragmentManager, final List<Meme> memes) {
        super(fragmentManager, memes);
        mContext = context;
    }

    @Override
    protected Fragment getFragmentForItem(final Meme meme) {
        return MemeViewPagerItemFragment.instantiateWithArgs(mContext, meme);
    }
}
```
</br>
</br>

Initialize your `ViewPager` and set the adapter to it. Also create a `CircularViewPagerHandler` and set it as `ViewPager.OnPageChangeListener` to the `ViewPager`:

```java 
final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
viewPager.setAdapter(new MemeCircularViewPagerAdapter(this, getSupportFragmentManager(), Meme.createSampleMemes()));
viewPager.setOnPageChangeListener(new CircularViewPagerHandler(viewPager));
```
</br>
</br>
If you would like to get track of the page change events, you can set your own `ViewPager.OnPageChangeListener` to the `CircularViewPagerHandler`.

```java 
final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
viewPager.setOnPageChangeListener(circularViewPagerHandler);
```

##How does it work
To achieve circular scrolling the `BaseCircularViewPagerAdapter` pretends to have two more items, a "copy" of the last item at the very first position and a "copy" of the first item at the very last position. This is realised by just increasing the value returned at the `getCount()` method of the adapter by 2 and handling to return the correct item for the current position. 

If the items would be *A* | *B* | *C*, the adapter actually "contains" *C* | *A* | *B* | *C* | *A*. The initial position of the adapter is 1, so it displays *A*. If the user now swipes left, the first *C* gets displayed. After *C* has settled, the current position of the `ViewPager` gets set to 3 by the `CircularViewPagerHandler`, so the second *C* gets displayed. The user can now swipe again and again until the procedure repeats. The same concept applies to the other direction. 

That's it, this enables circular scrolling for a `ViewPager`.

##Integration

The lib is available on Maven Central, you can find it with [Gradle, please](http://gradleplease.appspot.com/#circularviewpager)

```
dependencies {
    compile 'com.github.tobiasbuchholz:circularviewpager:1.0.0'
}
```

</br>
##Contribution
If you would like to contribute to this project make sure you send pull request to <b>dev</b> branch or create an issue.

##Developed by
* Tobias Buchholz - <tobias.buchholz89@gmail.com>

##License

    Copyright 2014 Tobias Buchholz
   
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
