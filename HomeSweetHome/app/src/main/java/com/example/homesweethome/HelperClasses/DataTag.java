package com.example.homesweethome.HelperClasses;

import androidx.annotation.NonNull;

public enum DataTag {
    TAG {
        @NonNull
        @Override
        public String toString() {
            return "tag";
        }
    },
    ADD {
        @NonNull
        @Override
        public String toString() {
            return "add";
        }
    },
    EDIT {
        @NonNull
        @Override
        public String toString() {
            return "edit";
        }
    },
    INPUT_TEXT {
        @NonNull
        @Override
        public String toString() {
            return "input";
        }
    },
    ARTIFACT_ID {
        @NonNull
        @Override
        public String toString() {
            return "artifact id";
        }
    },
    ARTIFACT_TITLE {
        @NonNull
        @Override
        public String toString() {
            return "title";
        }
    },
    ARTIFACT_DATE {
        @NonNull
        @Override
        public String toString() {
            return "date";
        }
    },
    ARTIFACT_VIDEO {
        @NonNull
        @Override
        public String toString() {
            return "video";
        }
    },
    IMAGE_ID {
        @NonNull
        @Override
        public String toString() {
            return "image id";
        }
    },
    IMAGE_PATH {
        @NonNull
        @Override
        public String toString() {
            return "image path";
        }
    }
}
