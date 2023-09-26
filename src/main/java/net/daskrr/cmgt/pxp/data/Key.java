package net.daskrr.cmgt.pxp.data;

public enum Key
{
    F1(97, 65535),
    F2(98, 65535),
    F3(99, 65535),
    F4(100, 65535),
    F5(101, 65535),
    F6(102, 65535),
    F7(103, 65535),
    F8(104, 65535),
    F9(105, 65535),
    F10(106, 65535),
    F11(107, 65535),
    F12(108, 65535),
    GRAVE(96, 96),
    N1(49, 49),
    N2(50, 50),
    N3(51, 51),
    N4(52, 52),
    N5(53, 53),
    N6(54, 54),
    N7(55, 55),
    N8(56, 56),
    N9(57, 57),
    N0(48, 48),
    MINUS(45, 45),
    EQUALS(61, 61),
    BACKSPACE(8, 8),
    TAB(9, 9),
    Q(81, 113),
    W(87, 119),
    E(69, 101),
    R(82, 114),
    T(84, 116),
    Y(89, 121),
    U(85, 117),
    I(73, 105),
    O(79, 111),
    P(80, 112),
    LEFT_BRACKET(91, 91),
    RIGHT_BRACKET(93, 93),
    ENTER(10, 1),

    CAPS_LOCK(20, 65535),
    A(65, 65),
    S(83, 83),
    D(68, 68),
    F(70, 70),
    G(71, 71),
    H(72, 72),
    J(74, 74),
    K(75, 75),
    L(76, 76),
    SEMICOLON(59, 59),
    QUOTE(39, 39),
    BACKSLASH(92, 92),

    SHIFT(16, 65535),
    Z(90, 90),
    X(88, 88),
    C(67, 67),
    V(86, 86),
    B(66, 66),
    N(78, 78),
    M(77, 77),
    COMMA(44, 44),
    DOT(46, 46),
    SLASH(47, 47),
    CTRL(17, 65535),
    ALT(18, 65535),
    SPACE(32, 3),
    ALT_GR(19, 65535),
    OPTIONS(153, 65535),
    ARROW_UP(38, 65535),
    ARROW_LEFT(37, 65535),
    ARROW_DOWN(40, 65535),
    ARROW_RIGHT(39, 65535),
    NUM_LOCK(148, 65535),
    NUM_SLASH(142, 47),
    NUM_ASTERISK(141, 42),
    NUM_MINUS(140, 45),
    NUM_7(135, 55),
    NUM_8(136, 56),
    NUM_9(137, 57),
    NUM_PLUS(139, 43),
    NUM_4(132, 52),
    NUM_5(133, 53),
    NUM_6(134, 54),
    NUM_1(129, 49),
    NUM_2(130, 50),
    NUM_3(131, 51),

    NUM_0(128, 48),
    NUM_DOT(138, 46);

    public final int code;
    public final int key;

    Key(int keyCode, int key) {
        this.code = keyCode;
        this.key = key;
    }

    public static Key fromCode(int code) {
        for (Key key : values())
            if (key.code == code)
                return key;

        return null;
    }
}
