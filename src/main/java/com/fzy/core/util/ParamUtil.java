package com.fzy.core.util;


/**
 * 参数处理类
 *
 * @author Fucai
 * @date 2018/3/19
 */
public class ParamUtil {


	public static Integer IntegerParam(Object object){
		Integer param = null;
		if(object instanceof String){
			try {
				param = Integer.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Integer){
			param = (Integer)object;
		}else if (object instanceof Long){
			param=((Long) object).intValue();
		}else if(object instanceof Double){
			param=((Double) object).intValue();
		}else if(object instanceof Float){
			param=((Float) object).intValue();
		}

		return param;
	}


	public static String StringParam(Object object){
		String param = null;
		if(object instanceof String){
			param = (String)object;
		}else if(object!=null){
			param=object.toString();
		}
		return param;
	}

	public static Long LongParam(Object object){
		Long param = null;
		if(object instanceof String){
			try {
				param = Long.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Integer ){
			param = Long.valueOf((Integer)object);
		}else if(object instanceof Long){
			param = (Long)object;
		}

		return param;
	}

	public static Double DoubleParam(Object object){
		Double param=null;
		if(object instanceof String){
			try {
				param=Double.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Double){
			param=(Double)object;
		}

		return param;
	}

	public static Float FloatParam(Object object){
		Float param=null;
		if(object instanceof String){
			try {
				param=Float.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Double){
			param=(Float) object;
		}
		return param;
	}

	public static Boolean BooleanParam(Object object){
		Boolean param=null;
		if(object instanceof String){
			try {
				param=Boolean.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Boolean){
			param=(Boolean)object;
		}
		return param;
	}

	public static Byte ByteParam(Object object){
		Byte param=null;
		if(object instanceof String){
			try {
				param=Byte.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Byte){
			param=(Byte)object;
		}
		return param;
	}

	public static Short ShortParam(Object object){
		Short param=null;
		if(object instanceof String){
			try {
				param=Short.valueOf((String)object);
			}catch (NumberFormatException e){
				return null;
			}
		}else if(object instanceof Short){
			param=(Short) object;
		}
		return param;
	}


}
