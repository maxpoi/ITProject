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
    CREATE {
        @NonNull
        @Override
        public String toString() {
            return "create";
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
    },
    DATE_SEPERATOR {
        @NonNull
        @Override
        public String toString() {
            return "/";
        }
    },
    DATE_PATTERN {
        @NonNull
        @Override
        public String toString() {
            return "\\d+";
        }
    },
    NEW_USER_EMAIL {
        @NonNull
        @Override
        public String toString() { return "newUserEmail"; }
    },
    NEW_USER_PASSWORD {
        @NonNull
        @Override
        public String toString() {
            return "newUserPassword";
        }
    },
    SCREEN_SHOT_PATH {
        @NonNull
        @Override
        public String toString() {
            return "screen shot path";
        }
    },
    BLANK_USER_INFO{
        @NonNull
        @Override
        public String toString() {
            return "blankUserInfo";
        }
    },
    EDIT_USER_INFO{
        @NonNull
        @Override
        public String toString() {
            return "EditUserInfo";
        }
    },
    SINGLE_ARTIFACT{
        @NonNull
        @Override
        public String toString() {
            return "visit_single_artifact";
        }
    }
}
