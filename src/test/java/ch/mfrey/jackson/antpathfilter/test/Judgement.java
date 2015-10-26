package ch.mfrey.jackson.antpathfilter.test;

import java.util.Date;

class Judgement {
    private int id;
    private String judgementNo;
    private Date judgementDate;
    private Courthouse courthouse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudgementNo() {
        return judgementNo;
    }

    public void setJudgementNo(String judgementNo) {
        this.judgementNo = judgementNo;
    }

    public Date getJudgementDate() {
        return judgementDate;
    }

    public void setJudgementDate(Date judgementDate) {
        this.judgementDate = judgementDate;
    }

    public Courthouse getCourthouse() {
        return courthouse;
    }

    public void setCourthouse(Courthouse courthouse) {
        this.courthouse = courthouse;
    }

    public static class Courthouse {
        private int id;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
