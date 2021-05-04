#include "com_googlecode_fannj_FannJNI.h"
#include "fann.h"

/*
 * Class:     com_googlecode_fannj_FannJNI
 * Method:    train
 * Signature: (J[F[F)V
 */
JNIEXPORT void JNICALL Java_com_googlecode_fannj_FannJNI_train
  (JNIEnv *env, jclass class, jlong annPtrValue, jfloatArray inputValues, jfloatArray outputValues)
{
    jfloat* input = (*env)->GetFloatArrayElements(env, inputValues, 0);
    jfloat* output = (*env)->GetFloatArrayElements(env, outputValues, 0);
    fann_train((struct fann *)annPtrValue, input, output);
    (*env)->ReleaseFloatArrayElements(env, inputValues, input, 0);
    (*env)->ReleaseFloatArrayElements(env, outputValues, output, 0);
}

/*
 * Class:     com_googlecode_fannj_FannJNI
 * Method:    reset_MSE
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_googlecode_fannj_FannJNI_reset_1MSE
  (JNIEnv *env, jclass class, jlong annPtrValue)
{
    fann_reset_MSE((struct fann *)annPtrValue);
}

/*
 * Class:     com_googlecode_fannj_FannJNI
 * Method:    get_MSE
 * Signature: (J)F
 */
JNIEXPORT jfloat JNICALL Java_com_googlecode_fannj_FannJNI_get_1MSE
  (JNIEnv * env, jclass class, jlong annPtrValue)
{
    return (jfloat) fann_get_MSE((struct fann *)annPtrValue);
}

