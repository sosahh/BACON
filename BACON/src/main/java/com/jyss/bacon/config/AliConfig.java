package com.jyss.bacon.config;

import java.io.Serializable;

public class AliConfig implements Serializable{
	private String URL = "https://openapi.alipay.com/gateway.do";
	private String APP_ID = "2018012902102531";
	private String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDF4HTCfANWMOK8HvAKmB0KyVosBe0wZCi21hPef7E7oEsU2eYHvxBFKG2ej8SatLmZ5uvzYGTsgW9DYqWPIvBdw5PbX3b9deL4zqdUdaWLmSUJidJcodMNrWaJ3ShxSk/RkA6FsLMDLKr+yQl/pBt11hP3y+En5LJZ5H+rzkFNNhK2btK5cvweVvYkc4PTdmSeSp6XiXFQg938PVkC4RSqDOajlxoLhjlPy9Bu72RHfc/+8T7a16sK78Rbj40cyLmwCuGUJI3XxjzItMkZhy23YefsZ3eY27mj9yP+3Wotpx7u5wGMgyQiT7IA0ptgHyDSDBQ1cHhj5+76FUoew54XAgMBAAECggEBAJ/rxbGg1HmGTNjJECsfwOkEznGh4I4phJYv8iLyhGSyKbwSVsyP6DBceiuZ+cUmppHxhWRO1YInIasOQMur6yN8ezTitKIn8mYvbiDebAqknMBWuMLQrmfxaluld5gH/PZbPMFji2nXYeNEyv5zvRXBDKZKChBZzqLD/0L1EOW6jZi0SUWkGsw2YG4VEd+8fZfW6id5WAJLehk/h+feqaNR6MC1536Zu23TdIPrS9tSvI7ycEuVZRDD7tNbaVrdOZes5J1c8RY6t/5gRSqycmZxicEPlmp0i/lwqHchsyfnMUwe8zevliEGc+ENg6dKpPsPcqm350zKYpuYgrVT6HECgYEA/R7CuWii06q3RptXbwnk0ifTFNm3a9CvJl8ZEivPJoe4x+4O3cqS+HfGCQxKDTI3kqzcj6sXpNFo1BDhfayckvnDX0zcJiRVdj2KGpnV9B66/CwMUkkno9CGoxQR6XiFN+auYxmm6lP2+siEKyxy42nUGWfL/ph8Sb0XneMaU8sCgYEAyCDLEhrRkTTzHWTfQmF3rbiODTIGVum7FF7t7PaSF5MmQG7Mc5A9Pbu74E8NeDoQAD0yOClt/Zd4xPg0GnJzcx86HomWibFQmC1rQa/mwNFYyPVe6JhpKs1u95DWQqF1khDy6pvLWU50dFRhjiyMDvreRnLDpUMJSAEScd31zWUCgYEAi0DcEhLd2aNG90JJBN6lk3SzbPomLdt+MJcK/PM7INB0ORHf/ecPmkCSo7GuH7pr1nbhVOzkshk/GcQ7ud83uKhN8uUfDKDan5VVoDLYAVjSuB8nnDGA7F+xYpCNFivm5o0zjXO0X6BX9TpRdEsc3b4AIQX4tZWAczTjhEpH1qcCgYBaCx8zSyvFiKxmKk+lf1Mor6QCXeKqUkmhNcEZYCuFPej1DqUgzeIwF3rFxuvJj15JrT8eM83QTsnA3egbiilVv9QXj1JTK5uWjMD4chuB3+9xo60LPB/9aScnblxo6FqM2OFjG6vXx8vv11N5c6Okzc0ap2JNxiaz5f+VigehXQKBgQCM7118ghOIDLjG4tIQrPxhrBZzsXU3cOzZL8+A8hStim/7VKOK2wzPzJp/aW9UfyJhE9AzCDo7SGgQkN20hOD2euboRChjbAV7KjjKWxeRQHSWS1YyjDhvk6vZDMb6VZ8gToh9vq7M6fHP8IeyvPsJpbw3c7jS8IP9t++cALOWQQ==";
	private String ALIPAY_PUBLIC_KEY = "";
	private String SIGN_TYPE = "RSA2";
	private String SELLER_ID = "2088921810014664";


	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getAPP_ID() {
		return APP_ID;
	}

	public void setAPP_ID(String APP_ID) {
		this.APP_ID = APP_ID;
	}

	public String getAPP_PRIVATE_KEY() {
		return APP_PRIVATE_KEY;
	}

	public void setAPP_PRIVATE_KEY(String APP_PRIVATE_KEY) {
		this.APP_PRIVATE_KEY = APP_PRIVATE_KEY;
	}

	public String getALIPAY_PUBLIC_KEY() {
		return ALIPAY_PUBLIC_KEY;
	}

	public void setALIPAY_PUBLIC_KEY(String ALIPAY_PUBLIC_KEY) {
		this.ALIPAY_PUBLIC_KEY = ALIPAY_PUBLIC_KEY;
	}

	public String getSIGN_TYPE() {
		return SIGN_TYPE;
	}

	public void setSIGN_TYPE(String SIGN_TYPE) {
		this.SIGN_TYPE = SIGN_TYPE;
	}

	public String getSELLER_ID() {
		return SELLER_ID;
	}

	public void setSELLER_ID(String SELLER_ID) {
		this.SELLER_ID = SELLER_ID;
	}
}
