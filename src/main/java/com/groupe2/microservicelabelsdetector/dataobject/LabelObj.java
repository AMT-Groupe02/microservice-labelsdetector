package com.groupe2.microserverdataobject.dataobject;

import java.util.List;

public class LabelObj {
    private final String name;
    private final float confidence;
    private final List<InstanceObj> instances;
    private final List<String> parents;

    public LabelObj(String name, float confidence, List<InstanceObj> instances, List<String> parents) {
        this.name = name;
        this.confidence = confidence;
        this.instances = instances;
        this.parents = parents;
    }

    public String getName() {
        return name;
    }

    public float getConfidence() {
        return confidence;
    }

    public List<InstanceObj> getInstances() {
        return instances; //TODO faire une copie?
    }

    public List<String> getParents() {
        return parents;
    }

    public static class InstanceObj {
        private final double confidence;
        private final BoundingBoxObj boundingBox;

        public InstanceObj(double confidence, BoundingBoxObj boundingBox) {
            this.confidence = confidence;
            this.boundingBox = boundingBox;
        }


        public static class BoundingBoxObj {
            private final double width;
            private final double height;
            private final double left;
            private final double top;

            public BoundingBoxObj(double width, double height, double left, double top) {
                this.width = width;
                this.height = height;
                this.left = left;
                this.top = top;
            }

            public double getWidth() {
                return width;
            }

            public double getHeight() {
                return height;
            }

            public double getLeft() {
                return left;
            }

            public double getTop() {
                return top;
            }
        }
    }
}
