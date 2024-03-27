// Add daily tracking if needed, such as a Map<Date, FreeThrowStats> 
public class Points {

    private int freeThrowsMade;
    private int freeThrowsAttempted;

    public Points(int freeThrowsMade, int freeThrowsAttempted) {
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    public int getFreeThrowsMade() {
        return freeThrowsMade;
    }

    public void setFreeThrowsMade(int freeThrowsMade) {
        this.freeThrowsMade = freeThrowsMade;
    }

    public int getFreeThrowsAttempted() {
        return freeThrowsAttempted;
    }

    public void setFreeThrowsAttempted(int freeThrowsAttempted) {
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    public void updateFreeThrowStats(int made, int attempted) {
        this.freeThrowsMade += made;
        this.freeThrowsAttempted += attempted;
    }

    public double getFreeThrowSuccessRate() {
        if (freeThrowsAttempted == 0) return 0;
        return ((double) freeThrowsMade / freeThrowsAttempted) * 100;
    }

    public class threePointShot {
        private int threePointersMade;
        private int threePointersAttempted;

        public threePointShot(int threePointersMade, int threePointersAttempted) {
            this.threePointersMade = threePointersMade;
            this.threePointersAttempted = threePointersAttempted;
        }

        public int getThreePointersMade() {
            return threePointersMade;
        }

        public void setThreePointersMade(int threePointersMade) {
            this.threePointersMade = threePointersMade;
        }

        public int getThreePointersAttempted() {
            return threePointersAttempted;
        }

        public void setThreePointersAttempted(int threePointersAttempted) {
            this.threePointersAttempted = threePointersAttempted;
        }

        public void updateThreePointStats(int made, int attempted) {
            this.threePointersMade += made;
            this.threePointersAttempted += attempted;
        }

        public double getThreePointSuccessRate() {
            if (threePointersAttempted == 0) return 0;
            return ((double) threePointersMade / threePointersAttempted) * 100;
        }
    }
}


