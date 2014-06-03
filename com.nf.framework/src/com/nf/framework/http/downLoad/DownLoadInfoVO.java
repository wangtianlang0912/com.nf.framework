package com.nf.framework.http.downLoad;
 /**
  *创建一个下载信息的实体类
  */
 public class DownLoadInfoVO {
     private int threadId;//下载器id
     private int startPos;//开始点
     private int totalFileSize;//文件总大小
     private int compeleteSize;//已完成下载的大小
     private String url;//下载器网络标识
     private int downLoadItemId;//下载文件id
     private int userId;
     
     public DownLoadInfoVO(int threadId, int startPos, int totalFileSize,
			int compeleteSize, String url,int downLoadItemId,int userId) {
		super();
		this.threadId = threadId;
		this.startPos = startPos;
		this.totalFileSize = totalFileSize;
		this.compeleteSize = compeleteSize;
		this.url = url;
		this.downLoadItemId=downLoadItemId;
		this.userId=userId;
	}

	public DownLoadInfoVO() {
     }
     public String getUrl() {
         return url;
     }
     public void setUrl(String url) {
         this.url = url;
     }
     public int getThreadId() {
         return threadId;
     }
     public void setThreadId(int threadId) {
         this.threadId = threadId;
     }
     public int getStartPos() {
         return startPos;
     }
     public void setStartPos(int startPos) {
         this.startPos = startPos;
     }
     
     public int getTotalFileSize() {
		return totalFileSize;
	}

	public void setTotalFileSize(int totalFileSize) {
		this.totalFileSize = totalFileSize;
	}

	public int getCompeleteSize() {
         return compeleteSize;
     }
     public void setCompeleteSize(int compeleteSize) {
         this.compeleteSize = compeleteSize;
     }
     
	public int getDownLoadItemId() {
		return downLoadItemId;
	}

	public void setDownLoadItemId(int downLoadItemId) {
		this.downLoadItemId = downLoadItemId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
     public String toString() {
         return "DownloadInfo [threadId=" + threadId
                 + ", startPos=" + startPos + ", totalFileSize=" + totalFileSize
                 + ", compeleteSize=" + compeleteSize +"]";
     }
 }