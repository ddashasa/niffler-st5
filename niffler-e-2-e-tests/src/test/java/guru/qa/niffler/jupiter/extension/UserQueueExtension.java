package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.model.UserJson.simpleUser;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.User.UserType;
import guru.qa.niffler.model.UserJson;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

// Любой тест проходит через него
public class UserQueueExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(UserQueueExtension.class);

    private static final Queue<UserJson> USERS = new ConcurrentLinkedQueue<>();

    private static final Map<UserType, Queue<UserJson>> userQueues = new EnumMap<>(User.UserType.class);

    static {
        userQueues.put(User.UserType.INVITATION_SEND, new ConcurrentLinkedQueue<>(Arrays.asList(
            simpleUser("duck", "12345")
        )));
        userQueues.put(User.UserType.INVITATION_RECIEVED, new ConcurrentLinkedQueue<>(Arrays.asList(
            simpleUser("alice", "12345")
        )));
        userQueues.put(User.UserType.WITH_FRIENDS, new ConcurrentLinkedQueue<>(Arrays.asList(
            simpleUser("charlie", "12345")
        )));
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Method testMethod = context.getRequiredTestMethod();
        List<Method> beforeAndTestMethods = new ArrayList<>();
        beforeAndTestMethods.add(testMethod);
        beforeAndTestMethods.addAll(Arrays.asList(context.getRequiredTestClass().getDeclaredMethods()));

        Map<User.UserType, List<UserJson>> usersByType = new HashMap<>();

        for (Method method : beforeAndTestMethods) {
            if (method.isAnnotationPresent(BeforeEach.class) || method.equals(testMethod)) {
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.isAnnotationPresent(User.class)) {
                        User userAnnotation = parameter.getAnnotation(User.class);
                        User.UserType userType = userAnnotation.value();
                        usersByType.putIfAbsent(userType, new ArrayList<>());
                        UserJson user = userQueues.get(userType).poll();
                        if (user != null) {
                            usersByType.get(userType).add(user);
                        }
                    }
                }
            }
        }

        context.getStore(NAMESPACE).put(context.getUniqueId(), usersByType);
        System.out.println("User types and their corresponding users for this test: " + usersByType);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Map<User.UserType, List<UserJson>> usersByType = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        if (usersByType != null) {
            usersByType.forEach((type, users) -> users.forEach(user -> userQueues.get(type).add(user)));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType().equals(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Map<User.UserType, List<UserJson>> usersByType = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        User userAnnotation = parameterContext.getParameter().getAnnotation(User.class);
        return usersByType.get(userAnnotation.value()).stream().findFirst().orElse(null);
    }
}
