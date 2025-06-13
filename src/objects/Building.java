package objects;

public class Building {
    private String name;
    private int price;
    private String unitType;
    private double unitPrice;
    private Building nextLevel;
    private int upgradePrice;
    private String upgradeName;
    private String upgradeUnit;
    private double upgradeUnitPrice;
    private boolean upgraded;

    public Building(String name, int price, String unitType, double unitPrice, String upgradeName, String upgradeUnit) {
        this.name = name;
        this.price = price;
        this.unitType = unitType;
        this.unitPrice = unitPrice;
        this.upgradeName = upgradeName;
        this.upgradeUnitPrice = unitPrice * 1.5;
        this.upgradePrice = price;
        this.upgradeUnit = upgradeUnit;
        upgraded = false;
    }

    public boolean isUpgraded() {
        return upgraded;
    }

    public boolean canUpgrade() {
        return nextLevel != null;
    }

    public Building upgrade() {
        return new Building(upgradeName, upgradePrice, upgradeUnit, upgradeUnitPrice, "", "");
    }

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public String getUnitType() {
        return unitType;
    }
    public double getUnitPrice() {
        return unitPrice;
    }
    public int getUpgradePrice() {
        return upgradePrice;
    }
    public String getUpgradeName() {
        return upgradeName;
    }
    public String getUpgradeUnit() {
        return upgradeUnit;
    }
    public double getUpgradeUnitPrice() {
        return upgradeUnitPrice;
    }
    public void setUpgraded(boolean upgrade) {
        this.upgraded = upgrade;
    }
}
