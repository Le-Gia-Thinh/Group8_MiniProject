package model.utils;

public class Constants {

    // Actions
    public static final String LOGIN_ACTION = "Login";
    public static final String LOGOUT_ACTION = "Logout";
    public static final String SEARCH_ACTION = "Search";
    public static final String UPDATE_ACTION = "Update";
    public static final String CREATE_ACTION = "Create";
    public static final String ADD_TO_CART_ACTION = "AddToCart";
    public static final String VIEW_CART_ACTION = "ViewCart";
    public static final String REMOVE_FROM_CART_ACTION = "RemoveFromCart";
    public static final String UPDATE_CART_ACTION = "UpdateCart";
    public static final String CHECKOUT_ACTION = "Checkout";
    public static final String CONFIRM_ORDER_ACTION = "ConfirmOrder";
    public static final String TRACK_ORDER_ACTION = "TrackOrder";

    // Pages
    public static final String LOGIN_PAGE = "login.jsp";
    public static final String SEARCH_PAGE = "search.jsp";
    public static final String UPDATE_PAGE = "admin/update.jsp";
    public static final String CREATE_PAGE = "admin/create.jsp";
    public static final String CART_PAGE = "cart.jsp";
    public static final String ORDER_TRACKING_PAGE = "order-tracking.jsp";
    public static final String ERROR_PAGE = "error.jsp";

    // Roles
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";

    // Pagination
    public static final int PRODUCTS_PER_PAGE = 20;

    // Login Google
    public static String GOOGLE_CLIENT_ID = "909740785037-q5g15o1mrs61fei85u4jvrvkqc7vao92.apps.googleusercontent.com";

    public static String GOOGLE_CLIENT_SECRET = "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";

    public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/Group8_BlindBoxStore/LoginGoogleHandler";

    public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";

    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    public static String GOOGLE_GRANT_TYPE = "authorization_code";
}
