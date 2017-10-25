#include <jni.h>
#include <string>
#include <bits/stdc++.h>
#include <stdlib.h>

using namespace std;

//p
int p=131;
//a
int a=2;
//b
int b=2;
//n
int n;
//h
int h;
//G
pair <int,int> par;
vector < pair < pair <int,int> , pair <int,int> > > v1;
vector < pair <int,int> > v;
//Sender public, Receiver public
pair <int,int> S,R;
//map the characters onto the curve points
map <char, pair <int,int> > m;
map <pair <int,int>, char> m1;
///////
pair <int,int> public_send(){
    int s_priv=29;
    //int s_priv=3;
    return v[(s_priv%(h+1)-1)];
}
pair <int,int> public_receive(){
    int r_priv=8;
    //int r_priv=4;
    return v[(r_priv%(h+1)-1)];
}
///////

int GCD(int u, int v){
    if (u == 0)
        return v;
    return GCD(v%u, u);
}

int mod(int s){
    if(s<0)
        s=(s%p)+p*2;
    s=s%p;
    return s;
}

int invmod(int s){
    int i=1;
    do{
        if((s*i)%p==1)
            return i;
        i++;
    }while(1);
}

pair <int,int> pointdouble(int x, int y){
    int s1=(3*x*x)+a;
    int s2=2*y;
    int s,gcd=GCD(abs(s1),abs(s2));
    s1/=gcd;
    s2/=gcd;

    if(s2<0){
        s1=-s1;
        s2=-s2;
    }

    s1=mod(s1);
    s2=invmod(s2);
    s=(s1*s2)%p;
    int x1=mod(s*s-2*x);
    return make_pair(x1,mod(s*(x-x1)-y));
}

pair <int,int> pointadd(int x1, int y1, int x, int y){

    if( (x1==0&&y1==0) || (x==0&&y==0) ){
        if(x1==0 && y1==0)
            return make_pair(x,y);
        else
            return make_pair(x1,y1);
    }

    if(x1==x && y1==y)
        return pointdouble(x1,y1);

    if(x1==x)
        return make_pair(0,0);

    int s1=y-y1;
    int s2=x-x1;
    int s,gcd=GCD(abs(s1),abs(s2));
    s1/=gcd;
    s2/=gcd;

    if(s2<0){
        s1=-s1;
        s2=-s2;
    }

    s1=mod(s1);
    s2=invmod(s2);
    s=(s1*s2)%p;
    int X=mod(s*s-x1-x);
    return make_pair(X,mod(s*(x1-X)-y1));
}

void generatepoints(){
    v.push_back(par);
    int x=par.first,y=par.second;
    pair <int,int> temp;
    temp=pointdouble(x,y);
    v.push_back(temp);
//	cout<<endl<<v[0].first<<v[0].second<<endl;
    while(1){
        x=temp.first;
        y=temp.second;
        temp=pointadd(x,y,par.first,par.second);
        v.push_back(temp);
        if(temp.first==par.first)
            break;
    }
}

//////
pair <int,int> sender_encryption(pair <int,int> M){
    pair <int,int> P=R;
    int s_priv=29;
    //int s_priv=3;
    if(s_priv>1){
        P=pointdouble(R.first,R.second);
        s_priv--;
        while(s_priv>1){
            P=pointadd(R.first,R.second,P.first,P.second);
            s_priv--;
        }
    }

    if(M==P) return pointdouble(P.first,P.second);
    return pointadd(M.first,M.second,P.first,P.second);
}
//////

//////
pair <int,int> receiver_decryption(pair <int,int> C2){
    pair <int,int> P=S;
    int r_priv=8;
    //int r_priv=4;
    if(r_priv>1){
        P=pointdouble(S.first,S.second);
        r_priv--;
        while(r_priv>1){
            P=pointadd(S.first,S.second,P.first,P.second);
            r_priv--;
        }
    }
    P.second=(p-P.second)%p;

    if(C2==P) return pointdouble(P.first,P.second);
    return pointadd(C2.first,C2.second,P.first,P.second);
}
//////


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_gauravpc_ecc_1project_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */,jstring msg) {
    std::string hello = "Hello from C++";

    int i,j;
    n=0;
    for(int i=0;i<p;i++){
        int z=pow(i,3)+a*i+b;
        int y=z%p;
        for(j=0;j<=p/2;j++)
            if(y==((j*j)%p)){
                v1.push_back(make_pair(make_pair(i,j),make_pair(i,(p-j)%p)));
                n+=2;
                if(p-j==p) n--;
                break;
            }
    }
    vector < pair < pair <int,int> , pair <int,int> > >::iterator it1=v1.begin();
    vector < pair < pair <int,int> , pair <int,int> > >:: reverse_iterator it2=v1.rbegin();
    ////mapping
    for(int i=0;i<128;i=i+2){
        m[i]=make_pair(it1->first.first + it1->first.second, it1->first.first - it1->first.second);
        m1[make_pair(it1->first.first + it1->first.second, it1->first.first - it1->first.second)]=i;
        if(it1->first.second==0){
            m[i+1]=make_pair(it2->second.first+it2->second.second,it2->second.first-it2->second.second);
            m1[make_pair(it2->second.first+it2->second.second,it2->second.first-it2->second.second)]=i+1;
        }

        else{
            m[i+1]=make_pair(it1->second.first+it1->second.second,it1->second.first-it1->second.second);
            m1[make_pair(it1->second.first+it1->second.second,it1->second.first-it1->second.second)]=i+1;
        }
        it1++;
    }
    par=v1[3].first;
    generatepoints();
    vector < pair <int,int> > :: iterator it;
    h=v.size();
    S=public_send();
    R=public_receive();
    pair <int,int> M,C2,RM;

    map <char, pair <int,int> > :: iterator mit;
    map <pair <int,int>, char> :: iterator mit1;


//    Jstring to C++ String
    if (!msg)
        return NULL;
    const jclass stringClass = env->GetObjectClass(msg);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(msg, getBytes, env->NewStringUTF("UTF-8"));
    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);
    std::string ret = std::string((char *)pBytes, length);
//    End converting

    std::cout<<ret;
    int l=ret.length();

    i=0;
    vector < pair<int,int> > send_encrypt_mssg;
    while(i<l){
        mit=m.find(ret[i++]);
        M=make_pair((mit->second.first+mit->second.second)/2,(mit->second.first-mit->second.second)/2);
        C2=sender_encryption(M);
        send_encrypt_mssg.push_back(C2); //vector encrypted data
    }

    string data;
    int size=send_encrypt_mssg.size();
    for(int i=0;i<size;i++){
        stringstream ss1,ss2;

        ss1<< send_encrypt_mssg[i].first;
        string str1= ss1.str();

        data+=str1;
        data+=",";

        ss2<< send_encrypt_mssg[i].second;
        string str2 = ss2.str();
        data+=str2;

        data+="\n";
    }
    return env->NewStringUTF(data.c_str());
}
