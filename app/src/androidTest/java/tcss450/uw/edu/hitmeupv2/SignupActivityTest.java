package tcss450.uw.edu.hitmeupv2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.allOf;
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
    public IntentsTestRule<SignupActivity> mActivityRule = new IntentsTestRule<>(SignupActivity.class);


    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("tcss450.uw.edu.hitmeupv2", appContext.getPackageName());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        String name = "testName";
        String username = generateRandomUserName();

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
        onView(withId(R.id.register)).perform(click());

        Thread.sleep(3000); // Wait for LoginActivity to appear
        intended(hasComponent(LoginActivity.class.getName())); //Test if the current activity is the login activity
    }

    @Test
    public void testDuplicateUsername() {
        String name = "testName";
        String existingUserName = "thaijaso";
        String phone = "0000000000";
        String password = "password";
        String confirmPassword = "password";

        onView(withId(R.id.nameEditText)).perform(typeText(name));
        onView(withId(R.id.phoneEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.phoneEditText)).perform(typeText(phone));
        onView(withId(R.id.usernameEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.usernameEditText)).perform(typeText(existingUserName));
        onView(withId(R.id.passwordEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText(password));
        onView(withId(R.id.passwordConfirm)).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordConfirm)).perform(typeText(confirmPassword));
        onView(withId(R.id.register)).perform(closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Username is taken")))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testEmptyName() {
        String name = "";
        String username = generateRandomUserName();
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
        onView(withId(R.id.register)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Please enter a valid name")))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testEmptyPhoneNumber() {
        String name = "test";
        String username = generateRandomUserName();
        String phone = "";
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
        onView(withId(R.id.register)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Please enter a valid phone number")))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testShortPassword() {
        String name = "test";
        String username = generateRandomUserName();
        String phone = "000000000";
        String password = "12345";
        String confirmPassword = "12345";

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
        onView(withId(R.id.register)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Password must be 6 characters long")))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testPasswordsNotMatching() {
        String name = "test";
        String username = generateRandomUserName();
        String phone = "000000000";
        String password = "password";
        String confirmPassword = "passwordd";

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
        onView(withId(R.id.register)).perform(click());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Passwords do not match")))
                .check(matches(isDisplayed()));
    }

    // Generate a (hopefully unique) random
    // username so that we can successfully register
    public String generateRandomUserName() {
        Random random = new Random();
        String username = "user"
                + (random.nextInt(400) + 1)
                + (random.nextInt(900) + 1)
                + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1)
                + (random.nextInt(100) + 1);

        return username;
    }

}
