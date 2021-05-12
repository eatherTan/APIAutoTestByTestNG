package util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class BuildFrameworkUtil {

    private static TestFile test;
    private static TestFile req;
    private static TestFile assertfile;
    private static TestFile constants;
    private static List<Map<String, Object>> parameters = new ArrayList<>();
    private static Class reqClass;

    /**
     *
     * @param packageName 指定生成的测试用例包名
     * @param dirFileName 指定生成的测试用例类名前缀
     * @param constantsFileName url所在的类名
     * @param request Request
     * @param response response
     * @param type 接口类型
     * @param url 接口url
     * @param comment 接口注释
     */
    public static void generateTest(String packageName, String dirFileName, String constantsFileName, Class request, Class response, String type, String url,String comment) {
        String pathTst = "com.example.testcase." + packageName;
        String pathReq = "com.example.request." + packageName;
        String pathAssert = "com.example.asserts." + packageName;
        createFramework(pathTst, pathReq, pathAssert, request, response, dirFileName, constantsFileName, type,url,comment);
    }

    public static void generateTestCusDir(String packageName,String reqPackageName,String AssPackageName,String DirFileName,String constantsFileName,Class request,Class response,String type,String url,String comment){
        String pathTst = "com.example.testcase." + packageName;
        String pathReq = "com.example.request." + reqPackageName;
        String pathAssert = "com.example.asserts." + AssPackageName;
        createFramework(pathTst, pathReq, pathAssert, request, response, DirFileName, constantsFileName, type, url,comment);
    }

    /**
     * @param pathTst       生成test路径 (com.example.testcase.服务名)
     * @param pathReq       生成request路径(com.example.request.服务名)
     * @param pathAssert    生成assert路径（com.example.assert.服务名)
     * @param request       接口入参request (SimpleLongRequest.class)
     * @param response      接口返回response (SimpleResponse.class)
     * @param fileName      要生成的文件名字  (OrderDetail)
     * @param constantsFile 接口url文件名 (服务名+Constants: OrderSvcConstants)
     * @param type          接口类型 (pi,pt,pbff,pb,cpb,h5pt)
     */
    public static void createFramework(String pathTst, String pathReq, String pathAssert, Class request, Class response, String fileName, String constantsFile, String type ,String url,String comment) {
        if (request != null) request = getRequest(request);
        constants = new TestFile(toFirstUpper(constantsFile),"com.example.constants",false);
        test = new TestFile(toFirstUpper(fileName) + "Test", pathTst,true);
        req = new TestFile(toFirstUpper(fileName) + "Request", pathReq,true);
        assertfile = new TestFile(toFirstUpper(fileName) + "Assert", pathAssert,true);
        constants.appendBody("/** " +"\n\t* " +comment +"\n\t*/");
        constants.appendBody("\n\tpublic static final String " + toFirstUpper(req.getName()) + " = \""+ url + "\";\n\n\t}");
        test.addList("import " + req.getFilePath() + "." + req.getFileName() + ";");
        test.addList("import org.testng.annotations.Test;");
        test.appendBody("\npublic class " + test.getFileName() + "{\r\n\r\n");
        test.appendBody("\t" + req.getFileName() + " request " + " = new " + req.getFileName() + "();\n");
        test.appendBody("\n\t@Test(groups={\"" + toFirstLower(test.getName() + "Test") + "\"})\r\n\tpublic void " + toFirstLower(test.getName()) + "Test(){\r\n");
//        test.appendBody("\t\tlong taId = 840;\r\n");

        assertfile.appendBody("\npublic class " + assertfile.getFileName() + "{\r\n\r\n");
        assertfile.appendBody("\tpublic void " + toFirstLower(assertfile.getName()) + "(");
        req.addList("import com.example.base.TestBase.tb;");
        req.addList("import com.example.base.HeaderMap.hp;");
        req.addList("import java.util.HashMap;");
        req.addList("import com.example.data." + constantsFile + ";");
        req.addList("import com.example.proto.v1.UserProto.UserType;");
        req.addList("import util.ResponseResult;");
        req.addList("import org.testng.Assert;");
        req.addList("import " + assertfile.getFilePath() + "." + assertfile.getFileName() + ";");
        req.appendBody("\npublic class " + req.getFileName() + "{\r\n\n");
        req.appendBody("\tString url = " + constantsFile + "." + toFirstUpper(req.getName()) + ";\r\n");
        //req.appendBody("\tpublic static final String " + toFirstLower(req.getName())+" = \""+url+"\";\r\n");
        req.appendBody("\t" + assertfile.getFileName() + " " + toFirstLower(assertfile.getFileName()) + " = new " + assertfile.getFileName() + "();\n");
        req.appendBody("\tpublic void commonRequest (");
        if (request != null) addParameters(request);
        test.appendBody("\t\tint errorCode = 0;\n");
        addParameter();
//        req.appendBody("\t\tHashMap<String, String> map = hp.addSimpleHeader(taId, UserType.ta_VALUE);\n");
        String reqBody = "";
        switch (Optional.ofNullable(type).orElse("").toLowerCase()) {
            case "pt":
            case "h5pt":
                reqBody = "\t\tHashMap<String, String> map = hp.addHeaderinfo(133995, UserType.student_VALUE);\n";
                req.appendBody(reqBody);
                req.appendEnd("\t\t" + req.importPackage(response.getName()) + " response = ResponseResult.getResponse(tb.doJsonPost(");
                break;
            case "pb":
                req.appendEnd("\t\t" + req.importPackage(response.getName()) + " response = ResponseResult.getResponse(tb.doJsonPost(");
                break;
            case "cpb":
                req.appendEnd("\t\t" + req.importPackage(response.getName()) + " response = ResponseResult.getResponse(tb.doGet(");
                break;
            case "pi":
                req.appendBody("\t\tHashMap<String, String> map = hp.addSimpleHeader(284, UserType.ta_VALUE);\n");
                req.appendEnd("\t\t" + req.importPackage(response.getName()) + " response = ResponseResult.getResponse(tb.doJsonPost(");
                break;
            case "pbff":
                reqBody = "\t\tHashMap<String, String> map = hp.addSimpleHeader(133995, UserType.student_VALUE);\n";
                req.appendBody(reqBody);
                req.appendEnd("\t\t" + req.importPackage(response.getName()) + " response = ResponseResult.getResponse(tb.doJsonPost(");
                break;
            default:
                test.getFile().delete();
                req.getFile().delete();
                assertfile.getFile().delete();
                throw new RuntimeException("目前还不支持此接口类型：" + type);
        }
        if (request == null) {
            if (!"cpb".equals(type)) {
                req.appendEnd("null,");
            }
        } else {
            req.appendEnd("request.build(),");
        }
        if (StringUtils.isNotEmpty(reqBody) || reqBody=="") {
            req.appendEnd("url, map)," + req.importPackage(response.getName()) + ".class);\n");
        } else {
            if ("cpb".equals(type)) {
                req.appendEnd("url, null)," + req.importPackage(response.getName()) + ".class);\n");
            } else {
                req.appendEnd("url)," + req.importPackage(response.getName()) + ".class);\n");
            }
        }
        req.appendEnd("\t\tint error_code = response.getResponse().getErrorCode();\n\t\tif(errorCode == 0 && error_code == 0){\n\t");
        req.appendEnd("\t\t" + toFirstLower(assertfile.getFileName()) + "." + toFirstLower(assertfile.getName()) + "();\n\t\t");
        req.appendEnd("}else {\n\t\t\tAssert.assertEquals(error_code,errorCode);\n\t\t}\n\t}\n\n}");
        test.appendBody("\t}\n\n}");
        assertfile.appendEnd("){\n\t}\n}");
        req.create();
        test.create();
        assertfile.create();
        constants.replaceFile();
    }

    private static String getVariateName(String className) {
        String[] m = className.replaceAll("\\$", "\\.").split("\\.");
        String name = toFirstLower(m[m.length - 2]);
        return className.equals(reqClass.getName()) ? "request" : name;
    }

    private static String getParameter(Class type, String parameterName) {
        Map<String, Object> map = new HashMap<>();
        if (type.isEnum()) {
            test.importPackage(type.getName());
            String enumName = req.importPackage(type.getName());
            test.appendBody("\t\t" + enumName + " " + parameterName + " = " + enumName + "." + type.getEnumConstants()[0] + ";\r\n");
            map.put("type", enumName);
        } else {
            String[] m = type.getName().replaceAll("\\$", "\\.").split("\\.");
//            if (parameterName.startsWith("qingqing") && m[m.length - 1].equals("String")) {
//                parameterName = toFirstLower(parameterName.replace("qingqing", ""));
//                test.appendBody("\t\tlong " + parameterName + " = 1L;\r\n");
//                map.put("type", "long");
//            } else {
            test.appendBody("\t\t" + m[m.length - 1] + " " + parameterName + " = ");
            switch (m[m.length - 1]) {
                case "long":
                    test.appendBody("1L;\r\n");
                    break;
                case "float":
                    test.appendBody("1f;\r\n");
                    break;
                case "int":
                case "double":
                    test.appendBody("1;\r\n");
                    break;
                case "boolean":
                    test.appendBody("true;\r\n");
                    break;
                case "String":
                    test.appendBody("\"\";\r\n");
                    break;
            }
            map.put("type", m[m.length - 1]);
        }
//        }
        map.put("name", parameterName);
        parameters.add(map);
        return parameterName;
    }

    private static String addParameters(Class request) {
        String variateName = getVariateName(request.getName());
        req.appendEnd("\t\t" + req.importPackage(request.getName()) + " " + variateName + " = " + req.importPackage(request.getName().replace("$Builder", "")) + ".newBuilder();\r\n");
        Method[] methods = request.getMethods();
        for (Method method : methods) {
            if (method.getReturnType().getName().equals(request.getName()) && method.getParameterTypes().length == 1) {
                String name = method.getName();
                if (name.substring(0, 3).equals("set")) {
                    Class type = method.getParameterTypes()[0];
                    if (isParameterObject(type)) {
                        if (type.getName().contains("Builder")) {
                            String variateName1 = addParameters(type);
                            req.appendEnd("\t\t" + variateName + "." + name + "(" + variateName1 + ");\r\n");
                        }
                    } else {
                        req.appendEnd("\t\t" + variateName + "." + method.getName() + "(");
                        String parameterName = getParameterName(name);
                        String pName = getParameter(type, parameterName);
                        if (pName.equals(parameterName)) {
                            req.appendEnd(pName);
                        } else {
                            addQQParameter(pName);
                            req.appendEnd(pName + ")");
                        }
                        req.appendEnd(");\r\n");
                    }
                } else if (name.substring(0, 3).equals("add")) {
                    Class type = method.getParameterTypes()[0];
                    if (!req.list.contains("import java.util.List;")) {
                        req.addList("import java.util.List;");
                    }
                    if (!test.list.contains("import java.util.List;")) {
                        test.addList("import java.util.List;");
                    }
                    if (!test.list.contains("import java.util.ArrayList;")) {
                        test.addList("import java.util.ArrayList;");
                    }
                    if (isParameterObject(type)) {
                        if (type.getName().contains("Builder")) {
                            String typeName = req.importPackage(type.getName());
                            req.appendEnd("\t\tfor( " + typeName + " " + toFirstLower(typeName) + " : " + toFirstLower(typeName) + "s){\n");
                            req.appendEnd("\t\t\t" + variateName + "." + name + "(" + toFirstLower(typeName) + ");\r\n\t\t}\n");
                            Map<String, Object> map = new HashMap<>();
                            map.put("type", typeName);
                            map.put("name", toFirstLower(typeName) + "s");
                            parameters.add(map);
                        }
                    } else {
                        String parameterName = getParameterName(name);
                        Map<String, Object> map = addParameterList(type, parameterName);
                        req.appendEnd("\t\tfor( " + map.get("cName") + " s : " + map.get("name") + "){\n");
                        if (parameterName.equals(map.get("name") + "") || (parameterName + "List").equals(map.get("name") + "")) {
                            req.appendEnd("\t\t\t" + variateName + "." + name + "(s);\n\t\t}\n");
                        } else {
                            req.appendEnd("\t\t\t" + variateName + "." + name + "(");
                            addQQParameter(map.get("name") + "");
                            req.appendEnd("s));\n\t\t}\n");
                        }
                    }
                }
            }
        }
        return variateName;
    }

    private static Map<String, Object> addParameterList(Class type, String parameterName) {
        if (!parameterName.endsWith("s")) {
            parameterName += "List";
        }
        Map<String, Object> map = new HashMap<>();
        if (type.isEnum()) {
            test.importPackage(type.getName());
            String enumName = req.importPackage(type.getName());
            test.appendBody("\t\tList<" + enumName + "> " + parameterName + " = new ArrayList<>();\n");
            test.appendBody("\t\t" + parameterName + ".add(" + enumName + "." + type.getEnumConstants()[0] + ");\n");
            map.put("type", "List<" + enumName + ">");
            map.put("cName", enumName);
        } else {
            String[] m = type.getName().replaceAll("\\$", "\\.").split("\\.");
            if (parameterName.contains("qingqing") && m[m.length - 1].equals("String")) {
                parameterName = toFirstLower(parameterName.replace("qingqing", ""));
                test.appendBody("\t\tList<Long> " + parameterName + " = new ArrayList<>();\n");
                test.appendBody("\t\t" + parameterName + ".add(1L);\r\n");
                map.put("type", "List<Long>");
                map.put("cName", "Long");
            } else {
                test.appendBody("\t\tList<" + toFirstUpper(m[m.length - 1]) + "> " + parameterName + " = new ArrayList<>();\n");
                test.appendBody("\t\t" + parameterName + ".add(");
                map.put("type", "List<" + toFirstUpper(m[m.length - 1]) + ">");
                map.put("cName", toFirstUpper(m[m.length - 1]));
                switch (m[m.length - 1]) {
                    case "long":
                        test.appendBody("1L);\r\n");
                        break;
                    case "float":
                        test.appendBody("1f);\r\n");
                        break;
                    case "int":
                    case "double":
                        test.appendBody("1);\r\n");
                        break;
                    case "boolean":
                        test.appendBody("true);\r\n");
                        break;
                    case "String":
                        test.appendBody("\"\");\r\n");
                        break;
                }
            }
        }
        map.put("name", parameterName);
        parameters.add(map);
        return map;
    }

    private static void addQQParameter(String parameterName) {
        if (parameterName.contains("teacher") || parameterName.contains("student") || parameterName.contains("customer") || parameterName.contains("parent") || parameterName.contains("assistant")) {
            req.addList("import com.example.utils.IdEncoder;");
            req.addList("import com.qingqing.common.auth.domain.UserType;");
            req.appendEnd("IdEncoder.encodeUser(");
            if (parameterName.contains("teacher")) {
                req.appendEnd("UserType.teacher,");
            } else if (parameterName.contains("student")) {
                req.appendEnd("UserType.student,");
            } else if (parameterName.contains("customer")) {
                req.appendEnd("UserType.crm_student,");
            } else if (parameterName.contains("user")) {
                req.appendEnd("UserType.crm_student,");
            } else if (parameterName.contains("assistant")) {
                req.appendEnd("UserType.ta,");
            } else if (parameterName.contains("parent")) {
                req.appendEnd("UserType.parent,");
            }
        } else {
            req.addList("import com.example.utils.OrderIdEncoder;");
            req.appendEnd("OrderIdEncoder.encodeOrderId(");
        }
    }

    private static void addParameter() {
//        test.appendBody("\t\trequest." + toFirstLower(req.getName()) + "(");
        test.appendBody("\t\trequest.commonRequest(");
//        req.appendBody("long taId,");
        for (Map<String, Object> map : parameters) {
            req.appendBody(map.get("type") + " " + map.get("name") + ",");
            test.appendBody(map.get("name") + ",");
        }
        test.appendBody("errorCode);\n");
        req.appendBody("int errorCode){\n");
    }

    private static boolean isParameterObject(Class type) {
        if (type.isEnum()) {
            return false;
        }
        String[] m = type.getName().replaceAll("\\$", "\\.").split("\\.");
        switch (m[m.length - 1]) {
            case "long":
            case "int":
            case "float":
            case "double":
            case "String":
            case "boolean":
                return false;
            default:
                return true;
        }
    }

    private static String getParameterName(String methodName) {
        String name = methodName.replace("set", "").replace("add", "");
        return toFirstLower(name);
    }

    private static Class getRequest(Class request) {
        try {
            reqClass = Class.forName(request.getName() + "$Builder");
            return reqClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileName(String url, String fileName) {
        if (fileName != null && !"".equals(fileName)) {
            return toFirstUpper(fileName);
        } else {
            String className = "";
            String[] m = url.split("/");
            String end = m[m.length - 1];
            if (end.contains("_")) {
                String[] m1 = end.split("_");
                for (String s : m1) {
                    className += toFirstUpper(s);
                }
            } else if (end.contains("-")) {
                String[] m1 = end.split("-");
                for (String s : m1) {
                    className += toFirstUpper(s);
                }
            } else {
                className = toFirstUpper(end);
            }
            return className;
        }
    }

    //首字母小写转换成大写
    private static String toFirstUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    //首字母大写转换成小写
    private static String toFirstLower(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }


    private static class TestFile {
        private String fileName;
        private File file;
        private String filePath;
        private StringBuffer head = new StringBuffer();
        private StringBuffer body = new StringBuffer();
        private StringBuffer end = new StringBuffer();
        private List<String> list = new ArrayList<>();

        public TestFile(String fileName, String filePath,boolean isAppend) {
            this.fileName = fileName;
//            if(fileName.contains("Req")){
//                filePath = filePath.replaceAll("testcase","request");
//            }
//            if(fileName.contains("Assert")){
//                filePath = filePath.replaceAll("testcase","asserts");
//            }
            this.filePath = filePath;
            if(isAppend){
                head.append("package " + filePath + ";\r\n\r\n");
            }
            try {
                this.file = createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        public File getFile() {
            return file;
        }

        private String importPackage(String name) {
            name = name.replaceAll("\\$", "\\.");
            String cName = "";
            if (name.contains("Builder")) {
                name = name.replaceAll("\\.Builder", "");
                cName = ".Builder";
            }
            String[] m = name.split("\\.");
            cName = m[m.length - 1] + cName;
            String importStr = "import " + name + ";";
            if (!list.contains(importStr)) {
                list.add(importStr);
            }
            return cName;
        }

        public void appendEnd(Object obj) {
            append(end, obj);
        }

        public void appendBody(Object obj) {
            append(body, obj);
        }

        public void addList(String str) {
            list.add(str);
        }

        public String getFileName() {
            return this.fileName;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public String getName() {
            if (fileName.contains("Test")) {
                return fileName.split("Test")[0];
            }
            if (fileName.contains("Req")) {
                return fileName.split("Req")[0];
            } else {
                return fileName.split("Assert")[0];
            }
        }

        //创建文件
        public void create() {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                for (String s : list) {
                    head.append(s + "\n");
                }
                head.append(body);
                head.append(end);
                fos.write(head.toString().getBytes());
                fos.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void appendFile(){
            RandomAccessFile file = null;
            try {
                file = new RandomAccessFile(fileName, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long len = 0;
            try {
                len = file.length();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long start = 0;
            try {
                start = file.getFilePointer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long nextend = start + len - 1;
            byte[] buf = new byte[1];
            try {
                file.seek(nextend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                file.read(buf, 0, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (buf[0] == '\n')
                try {
                    file.writeBytes(body.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else
                try {
                    file.writeBytes("\r\n" + body.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void replaceFile() {
            String str1 = "}";
            String str2 = body.toString();
            try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"))) {
                //数据流读取文件
                StringBuilder strBuilder = new StringBuilder();
                String temp;
                while ((temp = bufReader.readLine()) != null) {
                    if (temp.indexOf(str1) != -1) { //判断当前行是否存在想要替换掉的字符 -1表示存在
                        temp = temp.replaceAll(str1, str2);
                    }
                    strBuilder.append(temp);
                    strBuilder.append(System.lineSeparator());//行与行之间的分割
                }
                try (BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))){
                    bufWriter.write(strBuilder.toString());
                    bufWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private File createFile() throws IOException {
            File file;
            String path = "";
            if (fileName.contains("Req")) {
                path = System.getProperty("user.dir") + "/src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + fileName + ".java";
            }
            if (fileName.contains("Test")) {
                path = System.getProperty("user.dir") + "/src/test/java/" + filePath.replaceAll("\\.", "/") + "/" + fileName + ".java";
            }
            if (fileName.contains("Assert")) {
                path = System.getProperty("user.dir") + "/src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + fileName + ".java";
            }
            if (fileName.contains("Constant")) {
                path = System.getProperty("user.dir") + "/src/main/java/" + filePath.replaceAll("\\.", "/") + "/" + fileName + ".java";
            }

            file = new File(path);
            if (!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                System.out.println("目标文件所在目录不存在，准备创建它！");
                if (!file.getParentFile().mkdirs()) {
                    System.out.println("创建目标文件所在目录失败！");
                }
            }
            if (!file.exists()) {
                file.createNewFile();
            }
//            else {
//                if (fileName.contains("\\d")) {
//                    int i = fileName.charAt(fileName.length() - 1);
//                    fileName += i + 1;
//                } else {
//                    fileName += 1;
//                }
//                file = createFile();
//            }
            return file;
        }

        private void append(StringBuffer stringBuffer, Object obj) {
            stringBuffer.append(obj);
        }
    }

    public static class bd extends BuildFrameworkUtil {

    }

}
