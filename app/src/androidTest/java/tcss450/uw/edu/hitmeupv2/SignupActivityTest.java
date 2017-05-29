package tcss450.uw.edu.hitmeupv2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignupActivityTest {

    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test.
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */
    @Rule
    public ActivityTestRule<SignupActivity> mActivityRule = new ActivityTestRule<>(SignupActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("tcss450.uw.edu.hitmeupv2", appContext.getPackageName());
    }

    @Test
    public void testRegister() throws Exception {
        String name = "testName";
        String username = "test";
        String phone = "0000000000";
        String password = "password";
        String confirmPassword = "password";

        onView(withId(R.id.nameEditText)).perform(typeText(name));
        onView(withId(R.id.phoneEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(typeText(phone));
        onView(withId(R.id.usernameEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.usernameEditText)).perform(typeText(username));
        onView(withId(R.id.passwordEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText(password));
        onView(withId(R.id.passwordConfirm)).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordConfirm)).perform(typeText(confirmPassword));
        onView(withId(R.id.register)).perform(closeSoftKeyboard());
        //TODO: click the signup button

        //System.out.println(mActivityRule.getActivity());
        //intended(hasComponent(SignupActivity.class.getName()));
        //TODO: test if current current activity is the LoginActivity
    }

}
