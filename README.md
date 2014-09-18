CircularViewPager
=================

This is my implementation of a circular ViewPager. If you build the sample you will see an example.

You can also download the sample app in the Google Play Store: [demo](https://play.google.com/store/apps/details?id=com.tobishiba.circularviewpager)

<img src="https://chart.googleapis.com/chart?cht=qr&chl=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Dcom.tobishiba.circularviewpager&chs=180x180&choe=UTF-8&chld=L|2' alt=" />

[![Alt text for your video](http://img.youtube.com/vi/8L0gHiKz8fI/0.jpg)](https://www.youtube.com/watch?v=8L0gHiKz8fI&feature=youtu.be)

##Features
 - enables endless scrolling
 - very easy to use
 - no limitations, you can use it as a

##Usage

Add a usual ViewPager to you layout and set the overScrollMode to 'never':

```xml
	<android.support.v4.view.ViewPager
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:overScrollMode="never"/> 
```
</br>
</br>

Create an adapter for your ViewPager and let it extend the 'BaseCircularViewPagerAdapter' and implement it's methods:

```java 
public class MemeCircularViewPagerAdapter extends BaseCircularViewPagerAdapter<Meme> {
	... 	}
```
</br>
</br>

Initialize your ViewPager and set the adapter to it. Also create a 'CircularViewPagerHandler' and set it as ViewPager.OnPageChangeListener to the ViewPager:

```java 
final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
viewPager.setAdapter(new MemeCircularViewPagerAdapter(this, getSupportFragmentManager(), Meme.createSampleMemes()));
viewPager.setOnPageChangeListener(new CircularViewPagerHandler(viewPager));
```
</br>
</br>
If you would like to get track of the page change events, you can set your own ViewPager.OnPageChangeListener to the 'CircularViewPagerHandler'.

```java 
final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
viewPager.setOnPageChangeListener(circularViewPagerHandler);
```

</br>
##Contribution
If you would like to contribute to this project make sure you send pull request to <b>dev</b> branch or create an issue.

##Developed by
* Tobias Buchholz - <tobias.buchholz89@gmail.com>