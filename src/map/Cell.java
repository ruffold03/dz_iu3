package map;

public class Cell {
    protected String type;
    protected boolean isVisible;
    protected String feature;
    public String symbol;
    public String name;
    public int fine;
    public boolean stop;


    public Cell(String type) {
        this.type = type;
        this.isVisible = false;
        this.feature = "no";
    }

    public void custom(String name, int fine, String symbol, boolean stop) {
        this.name = name;
        this.fine = fine;
        this.symbol = symbol;
        this.stop = stop;
        this.type = "custom";
        this.feature = "no";
    }


    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double fine(String type, String feature) {
        double count = 1;
        if (!type.equals("path")) {
            if (feature.equals("tree")) {
                count = 2;
            }
            else {
                if (type.equals("custom")) {
                    count = fine;
                } else {
                    count = 1.5;
                }
            }
        }
        return count;
    }

    public String getSymbol() {
        if (!isVisible) {
            return "⬛ ";
        }

        if ("custom".equals(type)) {
            return symbol != null ? symbol : "? ";
        }

        if (!"no".equals(feature)) {
            return switch (feature) {
                case "tree" -> switch (type) {
                    case "snow" -> "\uD83C\uDF84 ";
                    case "desert" -> "\uD83C\uDF34 ";
                    default -> "\uD83C\uDF33 ";
                };
                case "mountain" -> switch (type) {
                    case "snow" -> "\uD83D\uDDFB ";
                    default -> "\uD83C\uDFD4\uFE0F ";
                };
                case "gold" -> "\uD83E\uDE99 ";
                default -> "\uD83D\uDFE9 ";
            };
        }


        return switch (type) {
            case "field" -> "\uD83D\uDFE9 ";
            case "enemy" -> "\uD83D\uDC80 ";
            case "castleHero" -> "\uD83C\uDFF0 ";
            case "castleEnemy" -> "\uD83D\uDD4C ";
            case "path" -> "\uD83D\uDFEB ";
            case "tree" -> "\uD83C\uDF32 ";
            case "mountain" -> "\uD83C\uDFD4\uFE0F ";
            case "sea" -> "\uD83D\uDFE6 ";
            case "desert" -> "\uD83D\uDFE8 ";
            case "snow" -> "⬜ ";
            case "snowTree" -> "\uD83C\uDF84 ";
            case "desertTree" -> "\uD83C\uDF34 ";
            case "snowMountain" -> "⛰\uFE0E ";
            case "wall" -> "\uD83E\uDDF1 ";
            case "stone" -> "\uD83E\uDEA8 ";
            case "hotel" -> "\uD83C\uDFE8";
            default -> "⬛ ";
        };
    }



    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isStop(){
        return stop;
    }

    public String getCustomSymbol() {
        return symbol;
    }

    public void setCustomSymbol(String symbol) {
        this.symbol = symbol;
    }

}
