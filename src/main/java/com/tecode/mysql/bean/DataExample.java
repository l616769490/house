package com.tecode.mysql.bean;

import java.util.ArrayList;
import java.util.List;

public class DataExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andValueIsNull() {
            addCriterion("value is null");
            return (Criteria) this;
        }

        public Criteria andValueIsNotNull() {
            addCriterion("value is not null");
            return (Criteria) this;
        }

        public Criteria andValueEqualTo(String value) {
            addCriterion("value =", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotEqualTo(String value) {
            addCriterion("value <>", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThan(String value) {
            addCriterion("value >", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThanOrEqualTo(String value) {
            addCriterion("value >=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThan(String value) {
            addCriterion("value <", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThanOrEqualTo(String value) {
            addCriterion("value <=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLike(String value) {
            addCriterion("value like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotLike(String value) {
            addCriterion("value not like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueIn(List<String> values) {
            addCriterion("value in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotIn(List<String> values) {
            addCriterion("value not in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueBetween(String value1, String value2) {
            addCriterion("value between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotBetween(String value1, String value2) {
            addCriterion("value not between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andXidIsNull() {
            addCriterion("xId is null");
            return (Criteria) this;
        }

        public Criteria andXidIsNotNull() {
            addCriterion("xId is not null");
            return (Criteria) this;
        }

        public Criteria andXidEqualTo(Integer value) {
            addCriterion("xId =", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidNotEqualTo(Integer value) {
            addCriterion("xId <>", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidGreaterThan(Integer value) {
            addCriterion("xId >", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidGreaterThanOrEqualTo(Integer value) {
            addCriterion("xId >=", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidLessThan(Integer value) {
            addCriterion("xId <", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidLessThanOrEqualTo(Integer value) {
            addCriterion("xId <=", value, "xid");
            return (Criteria) this;
        }

        public Criteria andXidIn(List<Integer> values) {
            addCriterion("xId in", values, "xid");
            return (Criteria) this;
        }

        public Criteria andXidNotIn(List<Integer> values) {
            addCriterion("xId not in", values, "xid");
            return (Criteria) this;
        }

        public Criteria andXidBetween(Integer value1, Integer value2) {
            addCriterion("xId between", value1, value2, "xid");
            return (Criteria) this;
        }

        public Criteria andXidNotBetween(Integer value1, Integer value2) {
            addCriterion("xId not between", value1, value2, "xid");
            return (Criteria) this;
        }

        public Criteria andLegendidIsNull() {
            addCriterion("legendId is null");
            return (Criteria) this;
        }

        public Criteria andLegendidIsNotNull() {
            addCriterion("legendId is not null");
            return (Criteria) this;
        }

        public Criteria andLegendidEqualTo(Integer value) {
            addCriterion("legendId =", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidNotEqualTo(Integer value) {
            addCriterion("legendId <>", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidGreaterThan(Integer value) {
            addCriterion("legendId >", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidGreaterThanOrEqualTo(Integer value) {
            addCriterion("legendId >=", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidLessThan(Integer value) {
            addCriterion("legendId <", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidLessThanOrEqualTo(Integer value) {
            addCriterion("legendId <=", value, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidIn(List<Integer> values) {
            addCriterion("legendId in", values, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidNotIn(List<Integer> values) {
            addCriterion("legendId not in", values, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidBetween(Integer value1, Integer value2) {
            addCriterion("legendId between", value1, value2, "legendid");
            return (Criteria) this;
        }

        public Criteria andLegendidNotBetween(Integer value1, Integer value2) {
            addCriterion("legendId not between", value1, value2, "legendid");
            return (Criteria) this;
        }

        public Criteria andXIsNull() {
            addCriterion("x is null");
            return (Criteria) this;
        }

        public Criteria andXIsNotNull() {
            addCriterion("x is not null");
            return (Criteria) this;
        }

        public Criteria andXEqualTo(String value) {
            addCriterion("x =", value, "x");
            return (Criteria) this;
        }

        public Criteria andXNotEqualTo(String value) {
            addCriterion("x <>", value, "x");
            return (Criteria) this;
        }

        public Criteria andXGreaterThan(String value) {
            addCriterion("x >", value, "x");
            return (Criteria) this;
        }

        public Criteria andXGreaterThanOrEqualTo(String value) {
            addCriterion("x >=", value, "x");
            return (Criteria) this;
        }

        public Criteria andXLessThan(String value) {
            addCriterion("x <", value, "x");
            return (Criteria) this;
        }

        public Criteria andXLessThanOrEqualTo(String value) {
            addCriterion("x <=", value, "x");
            return (Criteria) this;
        }

        public Criteria andXLike(String value) {
            addCriterion("x like", value, "x");
            return (Criteria) this;
        }

        public Criteria andXNotLike(String value) {
            addCriterion("x not like", value, "x");
            return (Criteria) this;
        }

        public Criteria andXIn(List<String> values) {
            addCriterion("x in", values, "x");
            return (Criteria) this;
        }

        public Criteria andXNotIn(List<String> values) {
            addCriterion("x not in", values, "x");
            return (Criteria) this;
        }

        public Criteria andXBetween(String value1, String value2) {
            addCriterion("x between", value1, value2, "x");
            return (Criteria) this;
        }

        public Criteria andXNotBetween(String value1, String value2) {
            addCriterion("x not between", value1, value2, "x");
            return (Criteria) this;
        }

        public Criteria andLegendIsNull() {
            addCriterion("legend is null");
            return (Criteria) this;
        }

        public Criteria andLegendIsNotNull() {
            addCriterion("legend is not null");
            return (Criteria) this;
        }

        public Criteria andLegendEqualTo(String value) {
            addCriterion("legend =", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendNotEqualTo(String value) {
            addCriterion("legend <>", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendGreaterThan(String value) {
            addCriterion("legend >", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendGreaterThanOrEqualTo(String value) {
            addCriterion("legend >=", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendLessThan(String value) {
            addCriterion("legend <", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendLessThanOrEqualTo(String value) {
            addCriterion("legend <=", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendLike(String value) {
            addCriterion("legend like", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendNotLike(String value) {
            addCriterion("legend not like", value, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendIn(List<String> values) {
            addCriterion("legend in", values, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendNotIn(List<String> values) {
            addCriterion("legend not in", values, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendBetween(String value1, String value2) {
            addCriterion("legend between", value1, value2, "legend");
            return (Criteria) this;
        }

        public Criteria andLegendNotBetween(String value1, String value2) {
            addCriterion("legend not between", value1, value2, "legend");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}